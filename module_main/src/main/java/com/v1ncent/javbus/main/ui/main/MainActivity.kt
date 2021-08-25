package com.v1ncent.javbus.main.ui.main

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.v1ncent.javbus.main.R
import com.v1ncent.javbus.main.BR
import com.v1ncent.javbus.main.databinding.MainActivityBinding

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/8/6
 * 描    述：
 * 修订历史：
 * ================================================
 */
@Route(path = com.v1ncent.javbus.base.config.C.RouterPath.Main.A_MAIN)
class MainActivity : com.v1ncent.javbus.base.core.BaseActivity<MainActivityBinding, MainViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.main_activity
    }

    override fun initVariableId(): Int {
        return BR.mainVM
    }
}