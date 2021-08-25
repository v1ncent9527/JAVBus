import com.v1ncent.javbus.buildsrc.AndroidX
import com.v1ncent.javbus.buildsrc.BuildVersion
import com.v1ncent.javbus.buildsrc.Deps
import com.v1ncent.javbus.buildsrc.Kotlin

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(BuildVersion.compileSdkVersion)
    buildToolsVersion(BuildVersion.buildToolsVersion)
    defaultConfig {
        minSdkVersion(BuildVersion.minSdkVersion)
        targetSdkVersion(BuildVersion.targetSdkVersion)
        versionCode(BuildVersion.versionCode)
        versionName(BuildVersion.versionName)
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                //阿里路由框架配置
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }

        ndk {
            abiFilters += setOf("armeabi-v7a")
        }

        ndkVersion = "22.1.7171670"

        vectorDrawables.useSupportLibrary = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
    }

    sourceSets.getByName("main") {
        jniLibs.srcDir("libs")
        res.srcDirs(mutableListOf("src/main/res", "src/main/res-night"))
    }
}

dependencies {
    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.4")
    debugImplementation("com.didichuxing.doraemonkit:dokitx:3.3.5")
    implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")

    //AndroidX
    api(AndroidX.constraintlayout)
    api(AndroidX.recyclerview)
    api(AndroidX.viewpager2)
    api(AndroidX.appcompat)
    api(AndroidX.material)
    api(AndroidX.flexbox)
    api(AndroidX.annotation)
    api(AndroidX.lifecycle_extensions)
    api(AndroidX.lifecycle_compiler)
    api(AndroidX.junit)
    api(AndroidX.runner)
    api(AndroidX.espresso_core)
    api(AndroidX.room_runtime)
    api(AndroidX.room_ktx)
    kapt(AndroidX.room_compiler)

    //Kotlin
    api(Kotlin.kotlin_stdlib)
    api(Kotlin.kotlin_core)
    api(Kotlin.kotlinx_coroutines_core)
    api(Kotlin.kotlinx_coroutines_android)

    //Deps
    api(Deps.rxkotlin)
    api(Deps.rxlifecycle)
    api(Deps.rxlifecycle_components)
    api(Deps.rxbinding)
    api(Deps.gson)
    api(Deps.lifecycle_extensions)
    kapt(Deps.lifecycle_compiler)
    api(Deps.arouter_api)
    kapt(Deps.arouter_compiler)
    api(Deps.autosize)
    api(Deps.bugly)
    api(Deps.immersionbar)
    api(Deps.immersionbar_components)
    api(Deps.okhttp3)
    api(Deps.net)
    api(Deps.logging_interceptor)
    api(Deps.smart_refresh_layout)
    api(Deps.smart_refresh_header)
    api(Deps.smart_refresh_footer)
    api(Deps.consecutiveScroller)
    api(Deps.skin_support)
    api(Deps.skin_support_appcompat)
    api(Deps.skin_support_cardview)
    api(Deps.skin_support_design)
    api(Deps.utilcodex)
    api(Deps.mmkv)
    api(Deps.coil)
    api(Deps.brv)
    api(Deps.xpopup)
}