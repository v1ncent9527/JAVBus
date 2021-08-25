package com.v1ncent.javbus.base.extension

import com.v1ncent.javbus.base.utils.MMKVUtils

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/8/25
 * 描    述：
 * 修订历史：
 * ================================================
 */

/**
 * 是否第一次进入APP
 */
private const val IS_FIRST_IN = "IS_FIRST_IN"
var isFirstIn
    get() = MMKVUtils.defaultHolder()!!.decodeBool(IS_FIRST_IN, true)
    set(value) {
        MMKVUtils.defaultHolder()!!.encode(IS_FIRST_IN, value)
    }

/**
 * 是否为黑夜模式
 */
private const val IS_NIGHT = "IS_NIGHT"
var isNight
    get() = MMKVUtils.defaultHolder()!!.decodeBool(IS_NIGHT)
    set(value) {
        MMKVUtils.defaultHolder()!!.encode(IS_NIGHT, value)
    }