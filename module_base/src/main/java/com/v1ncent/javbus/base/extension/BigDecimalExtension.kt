package com.v1ncent.javbus.base.extension

import com.blankj.utilcode.util.StringUtils
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/8/25
 * 描    述：BigDecimal相关
 * 修订历史：
 * ================================================
 */

/**
 * 负数为进一，正数为去尾
 *
 * @param scale 保留小数位数
 * @return
 */
fun Double.roundDown(scale: Int): String {
    val scaleType = if (this > 0) BigDecimal.ROUND_DOWN else BigDecimal.ROUND_UP
    var bigDecimal = BigDecimal(this.toString())
    val d2 = BigDecimal(1.toString())
    bigDecimal = bigDecimal.setScale(scale, scaleType)
    return bigDecimal.divide(d2).toPlainString()
}

fun String.roundDown(scale: Int): String {
    val scaleType = if (this.toDouble() > 0) BigDecimal.ROUND_DOWN else BigDecimal.ROUND_UP
    var bigDecimal = BigDecimal(this)
    val d2 = BigDecimal(1.toString())
    bigDecimal = bigDecimal.setScale(scale, scaleType)
    return bigDecimal.divide(d2).toPlainString()
}

fun Double.roundUp(scale: Int): String {
    var bigDecimal = BigDecimal(this.toString())
    val d2 = BigDecimal(1.toString())
    bigDecimal = bigDecimal.setScale(scale, BigDecimal.ROUND_UP)
    return bigDecimal.divide(d2).toPlainString()
}

fun String.roundUp(scale: Int): String {
    var bigDecimal = BigDecimal(this)
    val d2 = BigDecimal(1.toString())
    bigDecimal = bigDecimal.setScale(scale, BigDecimal.ROUND_UP)
    return bigDecimal.divide(d2).toPlainString()
}

/**
 * 保留到最后一位非0数字
 *
 * @return
 */
fun Double.formatZeroPlain(): String {
    return BigDecimal.valueOf(this).stripTrailingZeros().toPlainString()
}

fun String.formatZeroPlain(): String {
    return BigDecimal(this).stripTrailingZeros().toPlainString()
}

/**
 * 过滤多余的0
 *
 * @param s
 * @return
 */
fun String.subZeroAndDot(): String {
    if (!StringUtils.isTrimEmpty(this) && indexOf(".") > 0) {
        with(this) {
            replace("0+?$".toRegex(), "")
            replace("[.]$".toRegex(), "")
            return this
        }
    }
    return this
}

/**
 * 格式化数字 1234.5678 --> 1,234.5678
 *
 * @param groupingSize
 * @return
 */
fun String.formatByGroup(groupingSize: Int): String {
    if (!contains(".")) return this
    var front = split("\\.").toTypedArray()[0]
    val formatter = DecimalFormat()
    formatter.groupingSize = groupingSize
    front = formatter.format(front.toDouble())
    return front + "." + split("\\.").toTypedArray()[1]
}

fun Int.formatByGroup(groupingSize: Int): String {
    val formatter = DecimalFormat()
    formatter.groupingSize = groupingSize
    return formatter.format(this.toLong())
}

/**
 * 提供精确加法计算的add方法
 *
 * @param value1 被加数
 * @param value2 加数
 * @return 两个参数的和
 */
fun add(value1: Double, value2: Double): Double {
    val b1 = BigDecimal(value1.toString())
    val b2 = BigDecimal(value2.toString())
    return b1.add(b2).toDouble()
}

/**
 * 提供精确减法运算的sub方法
 *
 * @param value1 被减数
 * @param value2 减数
 * @return 两个参数的差
 */
fun sub(value1: Double, value2: Double): Double {
    val b1 = BigDecimal(value1.toString())
    val b2 = BigDecimal(value2.toString())
    return b1.subtract(b2).toDouble()
}

/**
 * 提供精确乘法运算的mul方法
 *
 * @param value1 被乘数
 * @param value2 乘数
 * @return 两个参数的积
 */
fun mul(value1: Double, value2: Double): Double {
    val b1 = BigDecimal(value1.toString())
    val b2 = BigDecimal(value2.toString())
    return b1.multiply(b2).toDouble()
}

/**
 * 提供精确的除法运算方法div
 *
 * @param value1 被除数
 * @param value2 除数
 * @param scale 精确范围
 * @return 两个参数的商
 */
@Throws(IllegalAccessException::class)
fun div(value1: Double, value2: Double, scale: Int): Double {
    // 如果精确范围小于0，抛出异常信息
    if (scale < 0) {
        throw IllegalAccessException("The accuracy cannot be less than 0")
    }
    val b1 = BigDecimal(value1.toString())
    val b2 = BigDecimal(value2.toString())
    return b1.divide(b2, scale, BigDecimal.ROUND_DOWN).toDouble()
}

/**
 * 提供精确的次方运算方法pow
 */
fun pow(value1: Double, value2: Int): Double {
    val b1 = BigDecimal(value1.toString())
    return b1.pow(value2).toDouble()
}

/**
 * 比较大小
 * -1 小于
 * 0  等于
 * 1  大于
 */
fun compare(s1: String, s2: String): Int {
    val b1 = BigDecimal(s1)
    val b2 = BigDecimal(s2)
    return b1.compareTo(b2)
}