package com.v1ncent.javbus.base.core

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.SkinAppCompatDelegateImpl
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.v1ncent.javbus.base.core.bus.receiveTag
import com.v1ncent.javbus.base.extension.loginOrNot
import com.v1ncent.javbus.base.model.ImmersionBarModel
import java.lang.reflect.ParameterizedType

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/7/6
 * 描    述：Activity基类
 * 修订历史：
 * ================================================
 */
abstract class BaseActivity<V : ViewDataBinding, VM : com.v1ncent.javbus.base.core.BaseViewModel> :
    RxAppCompatActivity(), com.v1ncent.javbus.base.core.IBaseView {

    lateinit var binding: V
    lateinit var viewModel: VM
    private var viewModelId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ARouter register
        ARouter.getInstance().inject(this)
        //私有的初始化DataBinding和ViewModel方法
        initViewDataBinding(savedInstanceState)
        //私有的ViewModel与View的契约事件回调逻辑
        registerVMLiveDataObserver()
        //init
        initStatusBar()
        initView()
        initData()
        initObservable()
    }

    override fun onDestroy() {
        super.onDestroy()
        //解除ViewModel生命周期感应
        lifecycle.removeObserver(viewModel)
        binding.unbind()
    }

    // 注册ViewModel与View的契约UI回调事件
    private fun registerVMLiveDataObserver() {
        //关闭界面
        viewModel.finishObserver.observe(this) {
            finish()
        }

        //加载对话框显示
        viewModel.loadingObserver.observe(this) {
//            loading(it)
        }

        //加载对话框消失
        viewModel.hideLoadingObserver.observe(this) {
//            hideLoading()
        }

        //登录状态监听
        receiveTag(com.v1ncent.javbus.base.config.C.BusTAG.LOGIN_STATUE) {
            onLoginObservable(loginOrNot())
        }
    }

    override fun initStatusBar() {
        val immersionBarModel = ImmersionBarModel()
        ImmersionBar.with(this)
            .titleBar(immersionBarModel.titleBarId)
            .statusBarColor(immersionBarModel.statusBarColor)
            .statusBarDarkFont(immersionBarModel.statusBarDarkFont)
            .navigationBarColor(immersionBarModel.navigationBarColor)
            .init()
    }

    override fun initView() {}
    override fun initData() {}
    override fun initObservable() {}
    override fun onLoginObservable(isLogin: Boolean) {}

    //-------------------------LoadingDialog-------------------------
//    private var loadingDialog: LoadingDialog? = null
//
//    open fun loading(title: String = "") {
//        if (!ObjectUtils.isEmpty(loadingDialog) && loadingDialog!!.isShow) return
//        if (ObjectUtils.isEmpty(loadingDialog)) {
//            loadingDialog = LoadingDialog(this, title)
//        } else {
//            loadingDialog?.setTitle(title)
//        }
//        XPopup.Builder(this)
//            .hasShadowBg(false)
//            .dismissOnTouchOutside(false)
//            .asCustom(loadingDialog)
//            .show()
//    }
//
//    open fun hideLoading() {
//        if (!ObjectUtils.isEmpty(loadingDialog) && loadingDialog!!.isShow)
//            loadingDialog?.smartDismiss()
//    }
    //-------------------------LoadingDialog-------------------------


    //-------------------------initViewDataBinding-------------------------
    /**
     * 注入绑定
     *
     * @param savedInstanceState
     */
    private fun initViewDataBinding(savedInstanceState: Bundle?) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState))
        viewModelId = initVariableId()
        viewModel = initViewModel()
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel)
        //让ViewModel拥有View的生命周期感应
        lifecycle.addObserver(viewModel)
        //注入RxLifecycle生命周期
        viewModel.injectLifecycleProvider(this)
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    abstract fun initContentView(savedInstanceState: Bundle?): Int

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    abstract fun initVariableId(): Int

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    private fun initViewModel(): VM {
        val modelClass: Class<*>
        val type = javaClass.genericSuperclass
        modelClass = if (type is ParameterizedType) {
            type.actualTypeArguments[1] as Class<*>
        } else {
            //如果没有指定泛型参数，则默认使用BaseViewModel
            com.v1ncent.javbus.base.core.BaseViewModel::class.java
        }
        return createViewModel<ViewModel>(this, modelClass) as VM
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
    </T> */
    private fun <T : ViewModel?> createViewModel(activity: FragmentActivity?, cls: Class<*>?): T {
        return ViewModelProviders.of(activity!!).get(cls as Class<T>)
    }

    //-------------------------initViewDataBinding-------------------------

    //-------------------------设置点击空白区域隐藏键盘-------------------------
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 根据 EditText 所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }

    //-------------------------设置点击空白区域隐藏键盘-------------------------

    //-------------------------字体大小不跟随系统-------------------------
    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }

    //-------------------------字体大小不跟随系统-------------------------
    override fun getDelegate(): AppCompatDelegate {
        return SkinAppCompatDelegateImpl.get(this, this)
    }
}
