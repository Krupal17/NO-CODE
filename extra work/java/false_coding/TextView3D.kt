package com.kp.softsavvy.futeres.no_code.false_coding

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat.setLayerType

class TextView3D(context: Context, attrs: AttributeSet? = null) :
    AppCompatTextView(context, attrs) {
    private var depth = 10f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val extrusionDepth = 10 // Depth for the 3D effect
    private var textureBitmap: Bitmap? = null // For applying texture
    private val shadowOffset = 5f // Shadow offset to enhance 3D effect

    init {
        // Enable hardware acceleration for better performance
        setLayerType(LAYER_TYPE_HARDWARE, paint)
    }

    // Method to set the texture (from a bitmap)
    fun setTexture(bitmap: Bitmap) {
        textureBitmap = bitmap
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Apply shadow based on position
//        paint.setShadowLayer(shadowRadius, shadowDx, shadowDy, Color.BLACK)
//        setLayerType(LAYER_TYPE_SOFTWARE, paint) // Ensure software rendering for shadows
        draw3DEffect(canvas)
    }

    private fun draw3DEffect(canvas: Canvas) {
        // Create a darker shadow for the depth effect
        paint.color = Color.MAGENTA
        paint.style = Paint.Style.FILL

        // Use an offset for depth to simulate thickness
        for (i in 1..depth.toInt()) {
            canvas.save()
            canvas.translate(i.toFloat(), i.toFloat())
            canvas.restore()
        }
    }

    //3D
//    override fun onDraw(canvas: Canvas) {
//
//        // Adjust shadow based on rotation
//        val shadowXOffset = shadowOffset * (rotationX / 360f)
//        val shadowYOffset = shadowOffset * (rotationY / 360f)
//
//        paint.setShadowLayer(shadowOffset, shadowXOffset, shadowYOffset, Color.BLACK)
//
//        // 3D text extrusion
//        for (i in 0 until extrusionDepth) {
//            paint.color = Color.DKGRAY
//            canvas.save()
//            canvas.translate(i.toFloat(), i.toFloat())
//            super.onDraw(canvas)
//            canvas.restore()
//        }
//
//        // Draw the final text with texture
//        paint.color = Color.WHITE
//        paint.shader = createTextureShader()
//        super.onDraw(canvas)
//    }


    // Create a BitmapShader for the texture
    private fun createTextureShader(): Shader? {
        textureBitmap?.let {
            return BitmapShader(it, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        }
        return null
    }


}

