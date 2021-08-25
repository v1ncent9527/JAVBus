package com.v1ncent.javbus.base.extension

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.blankj.utilcode.util.*
import com.google.gson.reflect.TypeToken
import com.v1ncent.javbus.base.R
import java.util.*

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/7/25
 * 描    述：通用Ext
 * 修订历史：
 * ================================================
 */


///////////////////////////////////////////////////////////////////////////
// ToastUtils
///////////////////////////////////////////////////////////////////////////

fun toast(toast: String) {
    ToastUtils.showShort(toast)
}

fun toast(resId: Int) {
    ToastUtils.showShort(StringUtils.getString(resId))
}

///////////////////////////////////////////////////////////////////////////
// AppUtils
///////////////////////////////////////////////////////////////////////////

/**
 * 是否未登录状态
 */
fun loginOrNot() = false

/**
 * 获取设备唯一标识
 *  1、BOARD   主板：The name of the underlying board, like "goldfish".
 *  2、BOOTLOADER 系统启动程序版本号：The system bootloader version number.
 *  3、BRAND  系统定制商：The consumer-visible brand with which the product/hardware will be associated, if any.
 *  4、CPU_ABI  cpu指令集：The name of the instruction set (CPU type + ABI convention) of native code.
 *  5、CPU_ABI2 cpu指令集2：The name of the second instruction set (CPU type + ABI convention) of native code.
 *  6、DEVICE 设备参数：The name of the industrial design.
 *  7、DISPLAY  显示屏参数：A build ID string meant for displaying to the user
 *  8、FINGERPRINT   唯一识别码：A string that uniquely identifies this build. Do not attempt to parse this value.
 *  9、HARDWARE   硬件名称：The name of the hardware (from the kernel command line or /proc).
 *  10、HOST
 *  11、ID  修订版本列表：Either a changelist number, or a label like "M4-rc20".
 *  12、MANUFACTURER  硬件制造商：The manufacturer of the product/hardware.
 *  13、MODEL  版本即最终用户可见的名称：The end-user-visible name for the end product.
 *  14、PRODUCT 整个产品的名称：The name of the overall product.
 *  15、RADIO  无线电固件版本：The radio firmware version number.   在API14后已过时。使用 getRadioVersion()代替。
 *  16、SERIAL 硬件序列号：A hardware serial number, if available. Alphanumeric only, case-insensitive.
 *  17、TAGS  描述build的标签,如未签名，debug等等。：Comma-separated tags describing the build, like "unsigned,debug".
 *  18、TIME
 *  19、TYPE build的类型：The type of build, like "user" or "eng".
 *  20、USER
 */
fun deviceID(): String {
    val deviceId = ("35"
            + Build.BOARD.length % 10
            + Build.BRAND.length % 10
            + Build.CPU_ABI.length % 10
            + Build.DEVICE.length % 10
            + Build.MANUFACTURER.length % 10
            + Build.MODEL.length % 10
            + Build.HARDWARE.length % 10
            + Build.PRODUCT.length % 10
            + Build.ID.length % 10)
    val fingerprint = Build.FINGERPRINT
    return UUID(deviceId.hashCode().toLong(), fingerprint.hashCode().toLong()).toString()
}

/**
 * 复制到剪贴板
 */
fun String?.copyText() {
    ClipboardUtils.copyText(this ?: "")
    toast(R.string.common_copy_to_clipboard)
}

///////////////////////////////////////////////////////////////////////////
// ImageUtils
///////////////////////////////////////////////////////////////////////////

/**
 * 保存图片到指定路径
 */
fun saveBitmap2Gallery(context: Context, bitmap: Bitmap): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val insert = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues()
        ) ?: return false

        context.contentResolver.openOutputStream(insert).use {
            it ?: return false
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return true
    } else {
        MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap,
            "save",
            System.currentTimeMillis().toString()
        )
        return true
    }
}

/**
 * 分享图片
 */
fun shareImage(context: Context, bmp: Bitmap) {
    val imageUri = Uri.parse(
        MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bmp,
            StringUtils.getString(R.string.common_share),
            StringUtils.getString(R.string.common_share)
        )
    )

    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
    shareIntent.type = "image/*"
    context.startActivity(
        Intent.createChooser(
            shareIntent, StringUtils.getString(R.string.common_share)
        )
    )
}

///////////////////////////////////////////////////////////////////////////
// LanguageUtils
///////////////////////////////////////////////////////////////////////////

/**
 * 获取当前语言
 */
fun currentLanguage(): String =
    when (LanguageUtils.getAppContextLanguage().toString()) {
        "zh_TW" -> "繁体中文"
        "en_US" -> "English"
        "ko_KR" -> "한국어"
        "ja_JP" -> "日本語"
        else -> "简体中文"
    }

/**
 * 获取当前语言请求头
 */
fun languageHeader(): String =
    when (LanguageUtils.getAppContextLanguage().toString()) {
        "zh_TW" -> "TW"
        "en_US" -> "US"
        "ko_KR" -> "KR"
        "ja_JP" -> "JP"
        else -> "CN"
    }

/**
 * 设置语言
 */
fun String.applyLanguage() {
    when (this) {
        "简体中文" -> LanguageUtils.applyLanguage(Locale.SIMPLIFIED_CHINESE, true)
        "English" -> LanguageUtils.applyLanguage(Locale.US, true)
    }
}

///////////////////////////////////////////////////////////////////////////
// GsonUtils
///////////////////////////////////////////////////////////////////////////

fun Any.toJson(): String = GsonUtils.toJson(this)

inline fun <reified T> String.toBean(): T =
    GsonUtils.fromJson(this, object : TypeToken<T>() {}.type)

///////////////////////////////////////////////////////////////////////////
// ResourceUtils
///////////////////////////////////////////////////////////////////////////

fun color(resId: Int): Int = ColorUtils.getColor(resId)

fun string(resId: Int): String = StringUtils.getString(resId)

fun stringArray(resId: Int): Array<out String>? = StringUtils.getStringArray(resId)

fun drawable(resId: Int): Drawable = ResourceUtils.getDrawable(resId)