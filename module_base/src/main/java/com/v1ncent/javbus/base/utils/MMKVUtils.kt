package com.v1ncent.javbus.base.utils

import android.content.Context
import android.os.Parcelable
import com.blankj.utilcode.util.AppUtils
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel
import java.util.*

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/7/8
 * 描    述：本地序列化工具类（MMKV）
 * 修订历史：
 * ================================================
 */
object MMKVUtils {
    // 日志 TAG
    private val TAG = MMKVUtils::class.java.simpleName

    // 持有类存储 Key-Holder
    private val HOLDER_MAP: MutableMap<String, Holder> = HashMap()

    // Default MMKV Holder
    private var DEFAULT_HOLDER: Holder? = null

    /**
     * 初始化方法 ( 必须调用 )
     *
     * @param context [Context]
     */
    fun init(context: Context?) {
        MMKV.initialize(
            context,
            if (AppUtils.isAppDebug()) MMKVLogLevel.LevelDebug else MMKVLogLevel.LevelNone
        )
    }
    // ===============
    // = 对外公开方法 =
    // ===============
    /**
     * 是否存在指定 Key 的 MMKV Holder
     *
     * @param key Key
     * @return `true` yes, `false` no
     */
    fun containsMMKV(key: String): Boolean {
        return HOLDER_MAP.containsKey(key)
    }

    /**
     * 通过 Key 获取 MMKV Holder
     *
     * @param key Key
     * @return [Holder]
     */
    operator fun get(key: String): Holder? {
        return if (containsMMKV(key)) HOLDER_MAP[key] else putHolder(key)
    }
    /**
     * 保存自定义 MMKV Holder
     *
     * @param key  Key
     * @param mmkv [MMKV]
     * @return [Holder]
     */
    /**
     * 保存自定义 MMKV Holder
     *
     * @param key Key
     * @return [Holder]
     */
    @JvmOverloads
    fun putHolder(
        key: String,
        mmkv: MMKV? = MMKV.mmkvWithID(key, MMKV.MULTI_PROCESS_MODE)
    ): Holder {
        val holder = Holder(mmkv)
        HOLDER_MAP[key] = holder
        return holder
    }

    /**
     * 获取 Default MMKV Holder
     *
     * @return [Holder]
     */
    fun defaultHolder(): Holder? {
        if (DEFAULT_HOLDER == null) {
            val mmkv = MMKV.mmkvWithID(TAG, MMKV.MULTI_PROCESS_MODE)
            DEFAULT_HOLDER = Holder(mmkv)
        }
        return DEFAULT_HOLDER
    }
    // =============
    // = 内部封装类 =
    // =============
    /**
     * detail: MMKV 持有类
     *
     * <pre>
     * 提供常用方法, 可根据需求自行添加修改或通过 [.getMMKV] 进行操作
    </pre> *
     */
    class Holder(
        /**
         * 获取 MMKV
         *
         * @return [MMKV]
         */
        // MMKV
        val mMKV: MMKV?
    ) {

        /**
         * 获取 MMKV mmap id
         *
         * @return mmap id
         */
        fun mmapID(): String? {
            return if (isMMKVEmpty) null else mMKV!!.mmapID()
        }
        // ===========
        // = 其他操作 =
        // ===========
        /**
         * 判断 MMKV 是否为 null
         *
         * @return `true` yes, `false` no
         */
        val isMMKVEmpty: Boolean
            get() = mMKV == null

        /**
         * 判断 MMKV 是否不为 null
         *
         * @return `true` yes, `false` no
         */
        val isMMKVNotEmpty: Boolean
            get() = mMKV != null

        /**
         * 是否存在指定 Key value
         *
         * @param key Key
         * @return `true` yes, `false` no
         */
        fun containsKey(key: String?): Boolean {
            if (isMMKVEmpty) return false
            return if (key.isNullOrEmpty()) false else mMKV!!.containsKey(key)
        }

        /**
         * 通过 key 移除 value
         *
         * @param key Key
         * @return `true` success, `false` fail
         */
        fun removeValueForKey(key: String?): Boolean {
            if (isMMKVEmpty) return false
            if (key.isNullOrEmpty()) return false
            mMKV!!.removeValueForKey(key)
            return true
        }

        /**
         * 通过 key 数组移除 value
         *
         * @param keys key 数组
         * @return `true` success, `false` fail
         */
        fun removeValuesForKeys(keys: Array<String?>?): Boolean {
            if (isMMKVEmpty) return false
            if (keys == null) return false
            mMKV!!.removeValuesForKeys(keys)
            return true
        }

        /**
         * 同步操作
         *
         * @return `true` success, `false` fail
         */
        fun sync(): Boolean {
            if (isMMKVEmpty) return false
            mMKV!!.sync()
            return true
        }

        /**
         * 异步操作
         *
         * @return `true` success, `false` fail
         */
        fun async(): Boolean {
            if (isMMKVEmpty) return false
            mMKV!!.async()
            return true
        }

        /**
         * 清除全部数据
         *
         * @return `true` success, `false` fail
         */
        fun clear(): Boolean {
            if (isMMKVEmpty) return false
            mMKV!!.clear()
            return true
        }

        // =======
        // = 存储 =
        // =======
        fun encode(
            key: String?,
            value: Boolean
        ): Boolean {
            if (isMMKVEmpty) return false
            return if (key.isNullOrEmpty()) false else mMKV!!.encode(key, value)
        }

        fun encode(
            key: String?,
            value: Int
        ): Boolean {
            if (isMMKVEmpty) return false
            return if (key.isNullOrEmpty()) false else mMKV!!.encode(key, value)
        }

        fun encode(
            key: String?,
            value: Long
        ): Boolean {
            if (isMMKVEmpty) return false
            return if (key.isNullOrEmpty()) false else mMKV!!.encode(key, value)
        }

        fun encode(
            key: String?,
            value: Float
        ): Boolean {
            if (isMMKVEmpty) return false
            return if (key.isNullOrEmpty()) false else mMKV!!.encode(key, value)
        }

        fun encode(
            key: String?,
            value: Double
        ): Boolean {
            if (isMMKVEmpty) return false
            return if (key.isNullOrEmpty()) false else mMKV!!.encode(key, value)
        }

        fun encode(
            key: String?,
            value: String?
        ): Boolean {
            if (isMMKVEmpty) return false
            return if (key.isNullOrEmpty()) false else mMKV!!.encode(key, value)
        }

        fun encode(
            key: String?,
            value: Set<String?>?
        ): Boolean {
            if (isMMKVEmpty) return false
            if (key.isNullOrEmpty()) return false
            return if (value == null) false else mMKV!!.encode(key, value)
        }

        fun encode(
            key: String?,
            value: ByteArray?
        ): Boolean {
            if (isMMKVEmpty) return false
            if (key.isNullOrEmpty()) return false
            return if (value == null) false else mMKV!!.encode(key, value)
        }

        fun encode(
            key: String?,
            value: Parcelable?
        ): Boolean {
            if (isMMKVEmpty) return false
            if (key.isNullOrEmpty()) return false
            return if (value == null) false else mMKV!!.encode(key, value)
        }

        // =======
        // = 读取 =
        // =======
        @JvmOverloads
        fun decodeBool(
            key: String?,
            defaultValue: Boolean = false
        ): Boolean {
            if (isMMKVEmpty) return defaultValue
            return if (key.isNullOrEmpty()) defaultValue else mMKV!!.decodeBool(
                key,
                defaultValue
            )
        }

        @JvmOverloads
        fun decodeInt(
            key: String?,
            defaultValue: Int = 0
        ): Int {
            if (isMMKVEmpty) return defaultValue
            return if (key.isNullOrEmpty()) defaultValue else mMKV!!.decodeInt(
                key,
                defaultValue
            )
        }

        @JvmOverloads
        fun decodeLong(
            key: String?,
            defaultValue: Long = 0L
        ): Long {
            if (isMMKVEmpty) return defaultValue
            return if (key.isNullOrEmpty()) defaultValue else mMKV!!.decodeLong(
                key,
                defaultValue
            )
        }

        @JvmOverloads
        fun decodeFloat(
            key: String?,
            defaultValue: Float = 0.0f
        ): Float {
            if (isMMKVEmpty) return defaultValue
            return if (key.isNullOrEmpty()) defaultValue else mMKV!!.decodeFloat(
                key,
                defaultValue
            )
        }

        @JvmOverloads
        fun decodeDouble(
            key: String?,
            defaultValue: Double = 0.0
        ): Double {
            if (isMMKVEmpty) return defaultValue
            return if (key.isNullOrEmpty()) defaultValue else mMKV!!.decodeDouble(
                key,
                defaultValue
            )
        }

        @JvmOverloads
        fun decodeString(
            key: String?,
            defaultValue: String? = null
        ): String? {
            if (isMMKVEmpty) return defaultValue
            return if (key.isNullOrEmpty()) defaultValue else mMKV!!.decodeString(
                key,
                defaultValue
            )
        }

        @JvmOverloads
        fun decodeStringSet(
            key: String?,
            defaultValue: Set<String>? = null,
            cls: Class<out MutableSet<*>?>? = HashSet::class.java
        ): Set<String>? {
            if (isMMKVEmpty) return defaultValue
            return if (key.isNullOrEmpty()) defaultValue else mMKV!!.decodeStringSet(
                key,
                defaultValue,
                cls
            )
        }

        @JvmOverloads
        fun decodeBytes(
            key: String?,
            defaultValue: ByteArray? = null
        ): ByteArray? {
            if (isMMKVEmpty) return defaultValue
            return if (key.isNullOrEmpty()) defaultValue else mMKV!!.decodeBytes(
                key,
                defaultValue
            )
        }

        fun <T : Parcelable?> decodeParcelable(
            key: String?,
            tClass: Class<T>?
        ): T? {
            return decodeParcelable(key, tClass, null)
        }

        fun <T : Parcelable?> decodeParcelable(
            key: String?,
            tClass: Class<T>?,
            defaultValue: T?
        ): T? {
            if (isMMKVEmpty) return defaultValue
            return if (key.isNullOrEmpty()) defaultValue else mMKV!!.decodeParcelable(
                key,
                tClass,
                defaultValue
            )
        }
    }
}