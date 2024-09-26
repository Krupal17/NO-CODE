package com.kp.softsavvy.futeres.no_code.finalcall

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
class MyGLRenderer : GLSurfaceView.Renderer {
    private lateinit var text3D: Text3DObject
    private val projectionMatrix = FloatArray(16) // 4x4 matrix for projection

    // These will store rotation values from the seekbars
    var rotationX = 0f
    var rotationY = 0f
    var rotationZ = 0f

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // Initialize your Text3DObject
        text3D = Text3DObject()

        // Clear screen color (optional)
        GLES20.glClearColor(0.2f, 0.2f, 0.2f, 1.0f) // Light black
    }

    override fun onDrawFrame(gl: GL10?) {
        // Clear the screen
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Set rotation values to the Text3DObject (from seekbars)
        text3D.rotationX = rotationX
        text3D.rotationY = rotationY
        text3D.rotationZ = rotationZ

        // Create a view matrix (camera)
        val viewMatrix = FloatArray(16)
        Matrix.setLookAtM(viewMatrix, 0,
            0f, 0f, 3f, // Camera position
            0f, 0f, 0f, // Look at point
            0f, 1f, 0f  // Up direction
        )

        // Pass the view and projection matrices to the Text3DObject for drawing
        text3D.draw(viewMatrix, projectionMatrix)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()

        // Set up a perspective projection matrix
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }
}
