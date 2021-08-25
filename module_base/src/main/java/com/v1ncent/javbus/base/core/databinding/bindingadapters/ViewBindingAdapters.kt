package com.v1ncent.javbus.base.core.databinding.bindingadapters

import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.blankj.utilcode.util.TimeUtils
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.longClicks
import com.v1ncent.javbus.base.extension.animateGone
import com.v1ncent.javbus.base.extension.animateInvisible
import com.v1ncent.javbus.base.extension.animateVisible
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/7/6
 * 描    述：
 * 修订历史：
 * ================================================
 */

@BindingConversion
fun visibility(visibility: Boolean) = if (visibility) View.VISIBLE else View.GONE

/**
 *  Animate view visibility true/false to View.VISIBLE and View.GONE
 *  @param visibility final visible state
 */
@BindingAdapter("animatedVisible")
fun View.animatedVisible(visibility: Boolean) {
    if (visibility) {
        animateVisible()
    } else {
        animateInvisible()
    }
}

/**
 *  Animate view visibility true/false to View.VISIBLE and View.INVISIBLE
 *  @param visibility final visible state
 */
@BindingAdapter("animatedVisibleGone")
fun View.animatedVisibleGone(visibility: Boolean) {
    if (visibility) {
        animateVisible()
    } else {
        animateGone()
    }
}

@BindingAdapter("background")
fun View.setBackground(@DrawableRes resId: Int) {
    ContextCompat.getDrawable(context, resId)?.also {
        this.background = it
    }
}

/**
 *  Animate android:layout_marginStart from original value to new value specified in pixels
 *  @param pixels final margin
 *  @param duration animation duration
 */
@BindingAdapter("animateMarginStart", "marginAnimDuration", requireAll = false)
fun View.animatedMarginStart(pixels: Int, duration: Int? = null) {
    (layoutParams as? ViewGroup.MarginLayoutParams)?.also { params ->
        ValueAnimator.ofInt(params.marginStart, pixels).apply {
            duration?.let { setDuration(it.toLong()) }
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                params.marginStart = it.animatedValue as? Int ?: 0
                layoutParams = params
            }

            start()
        }
    }
}

/**
 *  Animate android:layout_marginEnd from original value to new value specified in pixels
 *  @param pixels final margin
 *  @param duration animation duration
 */
@BindingAdapter("animateMarginEnd", "marginAnimDuration", requireAll = false)
fun View.animatedMarginEnd(pixels: Int, duration: Int? = null) {
    (layoutParams as? ViewGroup.MarginLayoutParams)?.also { params ->
        ValueAnimator.ofInt(params.marginEnd, pixels).apply {
            duration?.let { setDuration(it.toLong()) }
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                params.marginEnd = it.animatedValue as? Int ?: 0
                layoutParams = params
            }

            start()
        }
    }
}

/**
 *  Animate view background color with TransitionDrawable
 *  @param drawable TransitionDrawable
 *  @param duration animation duration
 */
@BindingAdapter("animBgTransition", "animBgDirectedDuration", requireAll = false)
fun View.animatedColor(drawable: Drawable, duration: Int? = null) {
    (drawable as? TransitionDrawable)?.also { transition ->
        background = transition
        when (duration) {
            null, 0 -> {
            }
            else ->
                if (duration < 0) {
                    if (this.tag == true) {
                        transition.reverseTransition(duration.absoluteValue)
                    }
                } else {
                    this.tag = true
                    transition.startTransition(duration)
                }
        }
    }
}

/**
 *  Animate view elevation from original value to new value specified in pixels
 *  @param pixels final elevation
 *  @param duration animation duration
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@BindingAdapter("animElevation", "animElevationDuration", requireAll = false)
fun View.animateElevation(pixels: Int, duration: Int? = null) {
    ValueAnimator.ofFloat(elevation, pixels.toFloat()).apply {
        duration?.let { setDuration(it.toLong()) }
        interpolator = DecelerateInterpolator()
        addUpdateListener {
            elevation = it.animatedValue as? Float ?: 0f
        }

        start()
    }
}

/**
 *  Set onFocusChangedListener
 *  @param listener View.OnFocusChangeListener
 */
@BindingAdapter("onFocusChanged")
fun View.onFocusChanged(listener: View.OnFocusChangeListener) {
    onFocusChangeListener = listener
}

//防重复点击间隔(毫秒)
const val CLICK_INTERVAL = 500

/**
 * requireAll 是意思是是否需要绑定全部参数, false为否
 * View的onClick事件绑定
 * onClickCommand 绑定的命令,
 * isThrottleFirst 是否开启防止过快点击
 */
@BindingAdapter(value = ["onClickCommand", "isThrottleFirst"], requireAll = false)
fun View.onClickCommand(clickCommand: com.v1ncent.javbus.base.core.databinding.command.BindingCommand, isThrottleFirst: Boolean) {
    if (isThrottleFirst) {
        clicks()
            .subscribe {
                clickCommand.execute()
            }
    } else {
        clicks()
            .throttleFirst(CLICK_INTERVAL.toLong(), TimeUnit.MILLISECONDS)
            .subscribe {
                clickCommand.execute()
            }
    }
}

/**
 * LongClick事件
 */
@BindingAdapter(value = ["onLongClickCommand"], requireAll = false)
fun View.onLongClickCommand(clickCommand: com.v1ncent.javbus.base.core.databinding.command.BindingCommand) {
    longClicks()
        .subscribe {
            clickCommand.execute()
        }
}

@BindingAdapter("android:enabled")
fun View.setEnableBind(enable: Boolean) {
    isEnabled = enable
}

@BindingAdapter("selected")
fun View.setSelectedBind(selected: Boolean) {
    isSelected = selected
}

/**
 * 根据毫秒值来显示时间
 */
@BindingAdapter(value = ["dateMilli", "dateFormat"], requireAll = false)
fun TextView.setDateFromMillis(milli: Long?, format: String? = "yyyy-MM-dd HH:mm:ss") {
    val finalFormat = if (format.isNullOrEmpty()) "yyyy-MM-dd HH:mm:ss" else format
    text = TimeUtils.millis2String(milli!!, finalFormat)
}