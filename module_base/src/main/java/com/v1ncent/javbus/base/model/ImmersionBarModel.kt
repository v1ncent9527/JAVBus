package com.v1ncent.javbus.base.model

import com.v1ncent.javbus.base.R


/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/7/14
 * 描    述：ImmersionBar属性相关
 * 修订历史：
 * ================================================
 */
class ImmersionBarModel(
    val titleBarId: Int = R.id.toolbar,
    val onlyInit: Boolean = false,
    val hideBar: Boolean = false,
    val statusBarColor: Int = R.color.white,
    val statusBarDarkFont: Boolean = true,
    val navigationBarColor: Int = R.color.white,
)