package com.kp.softsavvy.futeres.no_code.false_coding

import android.graphics.Color
import android.graphics.Rect
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
class TextStickerManager(private val parentLayout: FrameLayout) {

    private val stickers = mutableListOf<TextView3D>()
    private val shadowViews = mutableListOf<ShadowView>()

    fun addSticker(text: String): TextView3D {
        val sticker = TextView3D(parentLayout.context)
        val shadowView = ShadowView(parentLayout.context)

        // Add the sticker and shadow to the parent layout
        parentLayout.addView(shadowView)
        parentLayout.addView(sticker)

        stickers.add(sticker)
        shadowViews.add(shadowView)

        // Initialize the sticker
        sticker.text = text
//        sticker.layoutParams = FrameLayout.LayoutParams(
//            FrameLayout.LayoutParams.WRAP_CONTENT,
//            FrameLayout.LayoutParams.WRAP_CONTENT
//        )

        // Link shadow view with the sticker
        updateStickerShadow(sticker, shadowView)

        // Add listeners to update shadow when sticker properties change
        sticker.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateStickerShadow(sticker, shadowView)
        }

        return sticker
    }

    private fun updateStickerShadow(sticker: TextView3D, shadowView: ShadowView) {
        val stickerRect = Rect()
        sticker.getDrawingRect(stickerRect)
        val parentRect = Rect()
        parentLayout.getDrawingRect(parentRect)

        val dx = (stickerRect.left - parentRect.left).toFloat()
        val dy = (stickerRect.top - parentRect.top).toFloat()
        val radius = maxOf(
            parentRect.width() - stickerRect.right,
            stickerRect.left - parentRect.left,
            parentRect.height() - stickerRect.bottom,
            stickerRect.top - parentRect.top
        ) / 10f // Adjust shadow radius

        shadowView.updateShadow(dx, dy, radius)

        // Position shadow view behind the sticker
        val layoutParams = sticker.layoutParams as FrameLayout.LayoutParams
        shadowView.layoutParams = layoutParams
        shadowView.translationX = sticker.translationX
        shadowView.translationY = sticker.translationY
        shadowView.rotationX = sticker.rotationX
        shadowView.rotationY = sticker.rotationY
        shadowView.scaleX = sticker.scaleX
        shadowView.scaleY = sticker.scaleY
    }
}
