package com.v1ncent.javbus.main.ui.splash

import android.app.Application
import com.v1ncent.javbus.base.core.BaseViewModel
import com.v1ncent.javbus.base.core.bus.LiveDataEvent

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/8/6
 * 描    述：
 * 修订历史：
 * ================================================
 */
class SplashViewModel(application: Application) : BaseViewModel(application) {

    val test = LiveDataEvent<String>()
}