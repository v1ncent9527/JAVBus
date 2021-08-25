package com.v1ncent.javbus.base.core

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.trello.rxlifecycle2.LifecycleProvider
import com.v1ncent.javbus.base.R
import com.v1ncent.javbus.base.core.bus.LiveDataEvent
import com.v1ncent.javbus.base.extension.color
import java.lang.ref.WeakReference

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/7/6
 * 描    述：ViewModel基类
 * 修订历史：
 * ================================================
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application), IBaseViewModel {
    //弱引用持有
    private lateinit var lifecycle: WeakReference<LifecycleProvider<*>>

    //点击监听
    val clickObserver = LiveDataEvent<String>()

    //关闭界面
    val finishObserver = LiveDataEvent<Void>()

    //加载对话框
    val loadingObserver = LiveDataEvent<String>()
    val hideLoadingObserver = LiveDataEvent<Void>()

    //ToolBar
    //标题文字
    val titleText = ObservableField("")

    //左边文字
    val leftText = ObservableField("")
    val leftTextColor = ObservableField(color(R.color.black))

    //右边文字
    val rightText = ObservableField("")
    val rightTextColor = ObservableField(color(R.color.black))

    //是否可见
    val leftTextVisibleObservable = ObservableField(View.GONE)
    val leftIconVisibleObservable = ObservableField(View.VISIBLE)
    val rightTextVisibleObservable = ObservableField(View.GONE)
    val rightIconVisibleObservable = ObservableField(View.GONE)
    val bottomLineVisibleObservable = ObservableField(View.GONE)

    //点击事件
    var titleLeftOnClick =
        com.v1ncent.javbus.base.core.databinding.command.BindingCommand { titleLeftOnClick() }
    var titleRightOnClick =
        com.v1ncent.javbus.base.core.databinding.command.BindingCommand { titleRightOnClick() }


    open fun titleLeftOnClick() {
        finish()
    }

    open fun titleRightOnClick() {

    }

    /**
     * 注入RxLifecycle生命周期
     *
     * @param lifecycle
     */
    fun injectLifecycleProvider(lifecycle: LifecycleProvider<*>) {
        this.lifecycle = WeakReference(lifecycle)
    }

    val lifecycleProvider: LifecycleProvider<*>?
        get() = lifecycle.get()

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {}
    override fun onCreate() {}
    override fun onDestroy() {}
    override fun onStart() {}
    override fun onStop() {}
    override fun onResume() {}
    override fun onPause() {}
    override fun onCleared() {}

    open fun finish() {
        finishObserver.call()
    }

    open fun loading(title: String = "") {
        loadingObserver.value = title
    }

    open fun hideLoading() {
        hideLoadingObserver.call()
    }
}