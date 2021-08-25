import com.v1ncent.javbus.buildsrc.BuildVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

val isBuildModule: String by project

android {
    compileSdkVersion(BuildVersion.compileSdkVersion)
    buildToolsVersion(BuildVersion.buildToolsVersion)

    defaultConfig {
        applicationId(BuildVersion.applicationId)
        minSdkVersion(BuildVersion.minSdkVersion)
        targetSdkVersion(BuildVersion.targetSdkVersion)
        versionCode(BuildVersion.versionCode)
        versionName(BuildVersion.versionName)
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true

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
    signingConfigs {
        create("release") {
            storeFile = file("..\\javbus.jks")
            storePassword = "javbus9527"
            keyAlias = "javbus"
            keyPassword = "javbus9527"
        }
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    applicationVariants.all {
        outputs.all {
            if (name.contains("release")) {
                (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl)
                    .outputFileName = "JAVbus_release_V${versionName}.apk"
            } else {
                (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl)
                    .outputFileName = "JAVbus_debug_V${versionName}.apk"
            }
        }
    }

    sourceSets.getByName("main") {
        jniLibs.srcDir("libs")
        if (isBuildModule.toBoolean()) {
            manifest.srcFile("src/main/alone/AndroidManifest.xml")
        } else {
            resources {
                manifest.srcFile("src/main/AndroidManifest.xml")
                exclude("src/debug/*")
            }
        }
    }

    //统一资源前缀，规范资源引用
    resourcePrefix = "app_"

    buildFeatures {
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    lintOptions {
        isCheckReleaseBuilds = false
        isAbortOnError = false
    }
}

dependencies {
    implementation(project(":module_base"))

    if (!isBuildModule.toBoolean()) {
        implementation(project(":module_main"))
    }
}