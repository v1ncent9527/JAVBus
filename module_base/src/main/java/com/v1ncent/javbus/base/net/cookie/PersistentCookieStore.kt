package com.v1ncent.javbus.base.net.cookie

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.blankj.utilcode.util.LogUtils
import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.internal.and
import java.io.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/7/8
 * 描    述：
 * 修订历史：
 * ================================================
 */
open class PersistentCookieStore(context: Context) {
    private val cookies: MutableMap<String, ConcurrentHashMap<String?, Cookie>>
    private val cookiePrefs: SharedPreferences = context.getSharedPreferences(COOKIE_PREFS, 0)
    private fun getCookieToken(cookie: Cookie): String {
        return cookie.name + "@" + cookie.domain
    }

    fun add(url: HttpUrl, cookie: Cookie) {
        val name = getCookieToken(cookie)
        // 添加 host key. 否则有可能抛空.
        if (!cookies.containsKey(url.host)) {
            cookies[url.host] = ConcurrentHashMap()
        }
        // 删除已经有的.
        if (cookies.containsKey(url.host)) {
            cookies[url.host]!!.remove(name)
        }
        // 添加新的进去
        cookies[url.host]!![name] = cookie
        // 是否保存到 SP 中
        if (cookie.persistent) {
            val prefsWriter = cookiePrefs.edit()
            prefsWriter.putString(url.host, TextUtils.join(",", cookies[url.host]!!.keys))
            prefsWriter.putString(name, encodeCookie(SerializableOkHttpCookies(cookie)))
            prefsWriter.apply()
        } else {
            val prefsWriter = cookiePrefs.edit()
            prefsWriter.remove(url.host)
            prefsWriter.remove(name)
            prefsWriter.apply()
        }
    }

    fun addCookies(cookies: List<Cookie>) {
        for (cookie in cookies) {
            val domain = cookie.domain
            var domainCookies = this.cookies[domain]
            if (domainCookies == null) {
                domainCookies = ConcurrentHashMap()
                this.cookies[domain] = domainCookies
            }
        }
    }

    operator fun get(url: HttpUrl): List<Cookie> {
        val ret = ArrayList<Cookie>()
        if (cookies.containsKey(url.host)) ret.addAll(cookies[url.host]!!.values)
        return ret
    }

    fun removeAll() {
        val prefsWriter = cookiePrefs.edit()
        prefsWriter.clear()
        prefsWriter.apply()
        cookies.clear()
    }

    fun remove(url: HttpUrl, cookie: Cookie): Boolean {
        val name = getCookieToken(cookie)
        return if (cookies.containsKey(url.host) && cookies[url.host]!!.containsKey(name)) {
            cookies[url.host]!!.remove(name)
            val prefsWriter = cookiePrefs.edit()
            if (cookiePrefs.contains(name)) {
                prefsWriter.remove(name)
            }
            prefsWriter.putString(url.host, TextUtils.join(",", cookies[url.host]!!.keys))
            prefsWriter.apply()
            true
        } else {
            false
        }
    }

    fun getCookies(): List<Cookie> {
        val ret = ArrayList<Cookie>()
        for (key in cookies.keys) ret.addAll(cookies[key]!!.values)
        return ret
    }

    /**
     * cookies to string
     */
    private fun encodeCookie(cookie: SerializableOkHttpCookies?): String? {
        if (cookie == null) return null
        val os = ByteArrayOutputStream()
        try {
            val outputStream = ObjectOutputStream(os)
            outputStream.writeObject(cookie)
        } catch (e: IOException) {
            LogUtils.e("IOException in encodeCookie" + e.message)
            return null
        }
        return byteArrayToHexString(os.toByteArray())
    }

    /**
     * String to cookies
     */
    private fun decodeCookie(cookieString: String): Cookie? {
        val bytes = hexStringToByteArray(cookieString)
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        var cookie: Cookie? = null
        try {
            val objectInputStream = ObjectInputStream(byteArrayInputStream)
            cookie = (objectInputStream.readObject() as SerializableOkHttpCookies).getCookies()
        } catch (e: IOException) {
            LogUtils.e("IOException in decodeCookie" + e.message)
        } catch (e: ClassNotFoundException) {
            LogUtils.e("ClassNotFoundException in decodeCookie" + e.message)
        }
        return cookie
    }

    /**
     * byteArrayToHexString
     */
    private fun byteArrayToHexString(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (element in bytes) {
            val v: Int = element and 0xff
            if (v < 16) {
                sb.append('0')
            }
            sb.append(Integer.toHexString(v))
        }
        return sb.toString().uppercase(Locale.US)
    }

    /**
     * hexStringToByteArray
     */
    private fun hexStringToByteArray(hexString: String): ByteArray {
        val len = hexString.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(hexString[i], 16) shl 4) + Character.digit(
                hexString[i + 1], 16
            )).toByte()
            i += 2
        }
        return data
    }

    companion object {
        private const val COOKIE_PREFS = "Cookies_Prefs"
    }

    init {
        cookies = HashMap()
        val prefsMap = cookiePrefs.all
        for ((key, value) in prefsMap) {
            val cookieNames = TextUtils.split(value as String?, ",")
            for (name in cookieNames) {
                val encodedCookie = cookiePrefs.getString(name, null)
                if (encodedCookie != null) {
                    val decodedCookie = decodeCookie(encodedCookie)
                    if (decodedCookie != null) {
                        if (!cookies.containsKey(key)) {
                            cookies[key] = ConcurrentHashMap()
                        }
                        cookies[key]!![name!!] = decodedCookie
                    }
                }
            }
        }
    }
}