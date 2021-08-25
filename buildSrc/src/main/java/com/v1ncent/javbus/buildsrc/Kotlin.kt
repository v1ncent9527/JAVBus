package com.v1ncent.javbus.buildsrc

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/8/20
 * 描    述：kotlin 相关依赖
 * 修订历史：
 * ================================================
 */

private object KotlinVersion {
    const val kotlin_version = "1.5.20"
    const val kotlinx_coroutines_version = "1.3.9"
    const val kotlin_core_version = "1.3.2"
}

object Kotlin {
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${KotlinVersion.kotlin_version}"
    const val kotlin_core = "androidx.core:core-ktx:${KotlinVersion.kotlin_core_version}"

    //协程
    const val kotlinx_coroutines_core =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${KotlinVersion.kotlinx_coroutines_version}"
    const val kotlinx_coroutines_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${KotlinVersion.kotlinx_coroutines_version}"
}