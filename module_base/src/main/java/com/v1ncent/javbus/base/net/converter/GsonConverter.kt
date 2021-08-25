/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/8/9
 * 描    述：
 * 修订历史：
 * ================================================
 */

package com.v1ncent.javbus.base.net.converter

import com.drake.net.convert.NetConverter
import com.drake.net.exception.ConvertException
import com.drake.net.exception.RequestParamsException
import com.drake.net.exception.ResponseException
import com.drake.net.exception.ServerResponseException
import com.google.gson.GsonBuilder
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

class GsonConverter(
    val success: String = "200",    //后端定义为成功状态的错误码值
    val code: String = "code",      //错误码在JSON中的字段名
    val message: String = "message",//错误信息在JSON中的字段名
    val data: String = "data"       //model在JSON中的字段名
) : NetConverter {

    private val gson = GsonBuilder().serializeNulls().create()

    override fun <R> onConvert(succeed: Type, response: Response): R? {
        val code = response.code
        when {
            code in 200..299 -> { //请求成功
                val bodyString = response.body?.string() ?: return null
                return try {
                    val json = JSONObject(bodyString) //获取JSON中后端定义的错误码和错误信息
                    if (json.getString(this.code) == success) { //对比后端自定义错误码
                        json.getString(this.data).parseBody<R>(succeed)
                    } else { //错误码匹配失败, 开始写入错误异常
                        var errorMessage = json.optString(message, "no message")
                        when (json.getString(this.code).toInt()) {
//                            412 -> errorMessage =
//                                StringUtils.getString(com.bytelink.udunboss.module_base.R.string.common_code_error)
//                            20001 -> errorMessage =
//                                StringUtils.getString(com.bytelink.udunboss.module_base.R.string.common_code_fast)
                        }
                        throw ResponseException(response, errorMessage)
                    }
                } catch (e: JSONException) {
                    bodyString.parseBody<R>(succeed)
                }
            }
            //请求参数错误
            code in 400..499 -> throw RequestParamsException(
                response,
                code.toString()
            )
            //服务器异常错误
            code >= 500 -> throw ServerResponseException(
                response,
                code.toString()
            )
            else -> throw ConvertException(response)
        }
    }

    private fun <R> String.parseBody(succeed: Type): R? {
        return gson.fromJson(this, succeed)
    }
}