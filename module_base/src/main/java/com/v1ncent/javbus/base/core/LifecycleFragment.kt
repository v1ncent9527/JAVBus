package com.v1ncent.javbus.base.core

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.CallSuper
import com.trello.rxlifecycle2.components.support.RxFragment

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/7/6
 * 描    述：
 * 修订历史：
 * ================================================
 */
open class LifecycleFragment : RxFragment() {
    /**
     * Current visible state, combines self state and parental state.
     *
     *
     * Also used to avoid notify sate changes multiple times for the same state.
     */
    private var currentVisible = false

    /**
     * Flag to indicate onStart and onStop
     */
    private var started = false

    /**
     * First visible or not
     */
    private var firstVisible = true

    private val handler = Handler(Looper.getMainLooper())

    private val adjustStateRunnable = Runnable { adjustState() }

    override fun onStart() {
        super.onStart()
        started = true
        postAdjustState()
    }

    override fun onStop() {
        super.onStop()
        started = false
        postAdjustState()
    }

    @CallSuper
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        postAdjustState()
    }

    @CallSuper
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        postAdjustState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postAdjustState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        firstVisible = true
    }

    /**
     * Post adjust state to next message loop, otherwise sometimes the state flicks like this:<br></br>
     * onVisible -> onInvisible -> onVisible
     *
     *
     * For example, when used in nested ViewPager, each with a FragmentPagerAdapter,
     * when switches from Outer1-Inner1 to Outer4-Inner1, then switches back.
     * <pre>
     *
     *  1. Outer1-Inner1 will recreate, causes onVisible be invoked in onStart<br></br>
     *  1. The new created FragmentPagerAdapter.mCurrentPrimaryItem is null, so setUserVisibleHint(false)
     * will be called, causes onInvisible be invoked.
     *  1. Then ViewPager restores current item, call FragmentPagerAdapter.setPrimaryItem, so
     * setUserVisibleHint(true) will be called, causes onVisible be invoked again
     *
    </pre> *
     */
    private fun postAdjustState() {
        handler.removeCallbacks(adjustStateRunnable)
        handler.post(adjustStateRunnable)
    }

    /**
     * Adjust visible state and dispatch state change to children
     */
    private fun adjustState() {
        val visible = started && userVisibleHint && isVisible && isParentVisible()
        if (visible != currentVisible) {
            currentVisible = visible
            if (visible) {
                onVisible(firstVisible)
                firstVisible = false
                dispatchVisibleChanged()
            } else {
                dispatchVisibleChanged()
                onInvisible()
            }
        }
    }

    private fun dispatchVisibleChanged() {
        if (host == null) {
            return
        }
        val childFragmentManager = childFragmentManager
        val fragments = childFragmentManager.fragments
        for (fragment in fragments) {
            if (fragment is LifecycleFragment) {
                fragment.adjustState()
            }
        }
    }

    fun isCurrentVisible(): Boolean {
        return currentVisible
    }

    private fun isParentVisible(): Boolean {
        var visible = true
        var parent = parentFragment
        while (parent is LifecycleFragment && visible) {
            visible = parent.isCurrentVisible()
            parent = parent.getParentFragment()
        }
        return visible
    }

    /**
     * Fragment is visible to user
     *
     * @param firstVisible Whether is the first time being visible to user
     */
    open fun onVisible(firstVisible: Boolean) {}

    /**
     * Fragment is invisible to user
     */
    open fun onInvisible() {}
}