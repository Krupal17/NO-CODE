package com.kp.softsavvy.futeres.no_code.false_coding

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import androidx.core.view.ViewCompat.setLayerType
import com.kp.softsavvy.futeres.no_code.R

class ShadowView(context: Context) : View(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var shadowRadius = 10f // Shadow blur radius
    private var shadowDx = 0f // Shadow offset X
    private var shadowDy = 0f // Shadow offset Y

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, paint)
        setBackgroundColor(resources.getColor(android.R.color.transparent))
    }

    fun updateShadow(dx: Float, dy: Float, radius: Float) {
        shadowDx = dx
        shadowDy = dy
        shadowRadius = radius
        invalidate() // Redraw the view with the new shadow settings
    }

    override fun onDraw(canvas: Canvas) {

        // Apply shadow
        paint.setShadowLayer(shadowRadius, shadowDx, shadowDy, Color.BLACK)
//        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }
}
