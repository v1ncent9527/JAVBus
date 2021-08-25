package com.v1ncent.javbus.base.net.cookie

import android.annotation.SuppressLint
import android.content.Context
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/7/8
 * 描    述：
 * 修订历史：
 * ================================================
 */
class CookieManger(context: Context) : CookieJar {
    fun addCookies(cookies: List<Cookie>) {
        cookieStore!!.addCookies(cookies)
    }

    fun saveFromResponse(url: HttpUrl?, cookie: Cookie?) {
        if (cookie != null) {
            cookieStore!!.add(url!!, cookie)
        }
    }

    fun getCookieStore(): PersistentCookieStore? {
        return cookieStore
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (cookies.isNotEmpty()) {
            for (item in cookies) {
                cookieStore!!.add(url, item)
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore!![url]
    }

    fun remove(url: HttpUrl?, cookie: Cookie?) {
        cookieStore!!.remove(url!!, cookie!!)
    }

    fun removeAll() {
        cookieStore!!.removeAll()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var mContext: Context
        private var cookieStore: PersistentCookieStore? = null
    }

    init {
        mContext = context
        if (cookieStore == null) {
            cookieStore = PersistentCookieStore(mContext)
        }
    }
}