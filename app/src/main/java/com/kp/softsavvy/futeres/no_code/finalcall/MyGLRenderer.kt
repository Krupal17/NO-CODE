package com.kp.softsavvy.futeres.no_code.finalcall

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer : GLSurfaceView.Renderer {
    private lateinit var text3D: Text3DObject
    private val projectionMatrix = FloatArray(16) // 4x4 matrix for projection

    // Rotation values for controlling the text's orientation
    var rotationX = 0f
    var rotationY = 0f
    var rotationZ = 0f

    // Position and scale values for the text
    var positionX = 0f
    var positionY = 0f
    var positionZ = -5f // Default distance from the camera
    var scale = 1f

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // Initialize your Text3DObject
        text3D = Text3DObject().apply {
            // Set default properties
            text = "3D"
            setTextColor(1f, 1f, 1f, 1f) // White color
            setTextSize(32f) // Set default text size
            positionZ = -5f // Default distance from the camera
        }

        // Clear screen color (optional)
        GLES20.glClearColor(0.2f, 0.2f, 0.2f, 1.0f) // Light black
    }

    override fun onDrawFrame(gl: GL10?) {
        // Clear the screen
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Update the Text3DObject's properties based on user inputs
        text3D.rotationX = rotationX
        text3D.rotationY = rotationY
        text3D.rotationZ = rotationZ
        text3D.positionX = positionX
        text3D.positionY = positionY
        text3D.positionZ = positionZ
        text3D.scale = scale

        // Create a view matrix (camera)
        val viewMatrix = FloatArray(16)
        Matrix.setLookAtM(viewMatrix, 0,
            0f, 3f, 5f, // Move camera back to see the whole object
            0f, 0f, 0f,
            0f, 1f, 0f)

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
