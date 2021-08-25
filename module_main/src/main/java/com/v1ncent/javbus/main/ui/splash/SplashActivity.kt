package com.v1ncent.javbus.main.ui.splash

import android.os.Bundle
import com.v1ncent.javbus.main.R
import com.v1ncent.javbus.main.BR
import com.v1ncent.javbus.main.databinding.MainActivitySplashBinding

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/8/6
 * 描    述：
 * 修订历史：
 * ================================================
 */
class SplashActivity :
    com.v1ncent.javbus.base.core.BaseActivity<MainActivitySplashBinding, SplashViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.main_activity_splash
    }

    override fun initVariableId(): Int {
        return BR.splashVM
    }

}