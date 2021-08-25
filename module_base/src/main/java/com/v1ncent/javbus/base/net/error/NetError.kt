package com.v1ncent.javbus.base.net.error

import com.blankj.utilcode.util.StringUtils
import com.drake.net.exception.*
import com.v1ncent.javbus.base.R
import com.v1ncent.javbus.base.extension.toast
import java.net.UnknownHostException

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/7/9
 * 描    述：Net异常处理类
 * 修订历史：
 * ================================================
 */

/**
 * 提示
 */
fun Throwable.toast() {
    toast(msg)
}

/**
 * code
 */
val Throwable.code: Int
    get() {
        return when (this) {
            is ConvertException -> response.code
            is RequestParamsException -> response.code
            is ServerResponseException -> response.code
            else -> -1
        }
    }

/**
 * msg
 */
val Throwable.msg: String
    get() {
        return when (this) {
            is UnknownHostException -> StringUtils.getString(R.string.net_host_error)
            is URLParseException -> StringUtils.getString(R.string.net_url_error)
            is NetConnectException -> StringUtils.getString(R.string.net_network_error)
            is NetSocketTimeoutException -> StringUtils.getString(
                R.string.net_connect_timeout_error,
                message
            )
            is DownloadFileException -> StringUtils.getString(R.string.net_download_error)
            is ConvertException -> StringUtils.getString(R.string.net_parse_error)
            is RequestParamsException -> StringUtils.getString(R.string.net_request_error)
            is ServerResponseException -> StringUtils.getString(R.string.net_server_error)
            is NullPointerException -> StringUtils.getString(R.string.net_null_error)
            is NoCacheException -> StringUtils.getString(R.string.net_no_cache_error)
            is ResponseException -> message!!
            is NetException -> StringUtils.getString(R.string.net_error)
            else -> StringUtils.getString(R.string.net_other_error)
        }
    }