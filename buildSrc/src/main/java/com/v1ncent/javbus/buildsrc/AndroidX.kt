package com.v1ncent.javbus.buildsrc

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/8/20
 * 描    述：AndroidX 相关依赖
 * 最新版本可参考 :https://developer.android.com/jetpack/androidx/versions?hl=zh-cn
 * 修订历史：
 * ================================================
 */
private object AndroidXVersion {
    const val constraintlayout = "2.0.4"
    const val recyclerview = "1.2.0"
    const val viewpager2 = "1.0.0"
    const val appcompat = "1.2.0"
    const val material = "1.2.0"
    const val flexbox = "2.0.1"
    const val annotation = "1.2.0"
    const val lifecycle_version = "2.0.0"
    const val junit = "4.12"
    const val runner = "1.1.1"
    const val espresso_core = "3.1.0"
    const val room = "2.3.0"
}

object AndroidX {
    const val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${AndroidXVersion.constraintlayout}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${AndroidXVersion.recyclerview}"
    const val viewpager2 = "androidx.viewpager2:viewpager2:${AndroidXVersion.viewpager2}"
    const val appcompat = "androidx.appcompat:appcompat:${AndroidXVersion.appcompat}"
    const val material = "com.google.android.material:material:${AndroidXVersion.material}"
    const val flexbox = "com.google.android:flexbox:${AndroidXVersion.flexbox}"
    const val annotation = "androidx.annotation:annotation:${AndroidXVersion.annotation}"
    const val lifecycle_extensions =
        "androidx.lifecycle:lifecycle-extensions:${AndroidXVersion.lifecycle_version}"
    const val lifecycle_compiler =
        "androidx.lifecycle:lifecycle-compiler:${AndroidXVersion.lifecycle_version}"
    const val junit = "junit:junit:${AndroidXVersion.junit}"
    const val runner = "androidx.test.ext:junit:${AndroidXVersion.runner}"
    const val espresso_core =
        "androidx.test.espresso:espresso-core:${AndroidXVersion.espresso_core}"
    const val room_runtime = "androidx.room:room-runtime:${AndroidXVersion.room}"
    const val room_ktx = "androidx.room:room-ktx:${AndroidXVersion.room}"
    const val room_compiler = "androidx.room:room-compiler:${AndroidXVersion.room}"
}