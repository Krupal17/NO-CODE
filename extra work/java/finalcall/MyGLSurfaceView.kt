package com.kp.softsavvy.futeres.no_code.finalcall

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent

class MyGLSurfaceView(context: Context, attrs: AttributeSet?) : GLSurfaceView(context, attrs) {

    private val renderer: MyGLRenderer
    var previousX = 0f
    var previousY = 0f

    init {
        // Set the OpenGL version to 2.0
        setEGLContextClientVersion(2)

        // Initialize the renderer and set it to this view
        renderer = MyGLRenderer()
        setRenderer(renderer)

        // Optional: Set the render mode to only render when there's a change
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    // Pass the rotation and translation inputs from SeekBars to the renderer
    override fun setRotationX(rotation: Float) {
        renderer.rotationX=rotation
        requestRender()
    }

    override fun setRotationY(rotation: Float) {
        renderer.rotationY=rotation
        requestRender()

    }

    fun setRotationZ(rotation: Float) {
        renderer.rotationZ=rotation
        requestRender()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Calculate rotation based on touch movement
        Log.e("TAG", "onTouchEvent: --->$previousX  $previousY", )
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.x - previousX
                val deltaY = event.y - previousY

                setRotationX(rotationX + deltaY / 2)
                setRotationY(rotationY + deltaX / 2)


                previousX = event.x
                previousY = event.y
            }
        }
        return true
    }

}
