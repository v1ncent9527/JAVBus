package com.v1ncent.javbus.base.core.databinding.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/7/7
 * 描    述：
 * 修订历史：
 * ================================================
 */

/**
 * 设置图片Url地址
 * @param imgUrl         图片地址
 * @param placeholderRes 占位图
 */
@BindingAdapter(value = ["imgUrl", "placeholderRes"], requireAll = false)
fun ImageView.setImageUrl(imgUrl: String?, placeholderRes: Int) {
    load(imgUrl) {
        placeholder(placeholderRes)
        crossfade(true)
    }
}

/**
 * 设置圆形图片Url地址
 * @param circleImgUrl    图片地址
 * @param placeholderRes  占位图
 */
@BindingAdapter(value = ["circleImgUrl", "placeholderRes"], requireAll = false)
fun ImageView.setCircleImageUrl(circleImgUrl: String?, placeholderRes: Int) {
    load(circleImgUrl) {
        placeholder(placeholderRes)
        crossfade(true)
        transformations(CircleCropTransformation())
    }
}

/**
 * 设置圆角图片Url地址
 * @param roundedImgUrl  图片地址
 * @param placeholderRes 占位图
 * @param roundedTopLeft 圆角大小
 * @param roundedTopRight
 * @param roundedBottomLeft
 * @param roundedBottomRight
 */
@BindingAdapter(
    value = ["roundedImgUrl",
        "placeholderRes",
        "roundedTopLeft",
        "roundedTopRight",
        "roundedBottomLeft",
        "roundedBottomRight"], requireAll = false
)
fun ImageView.setRoundedImageUrl(
    roundedImgUrl: String?,
    placeholderRes: Int,
    roundedTopLeft: Float,
    roundedTopRight: Float,
    roundedBottomLeft: Float,
    roundedBottomRight: Float
) {
    load(roundedImgUrl) {
        placeholder(placeholderRes)
        crossfade(true)
        transformations(
            RoundedCornersTransformation(
                topLeft = roundedTopLeft,
                topRight = roundedTopRight,
                bottomLeft = roundedBottomLeft,
                bottomRight = roundedBottomRight
            )
        )
    }
}