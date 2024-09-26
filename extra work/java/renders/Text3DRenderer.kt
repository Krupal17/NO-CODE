// Text3DRenderer.kt
package com.kp.softsavvy.futeres.no_code.renders

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.kp.softsavvy.futeres.no_code.renders.Text3D
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
class Text3DRenderer(private val context: Context) : GLSurfaceView.Renderer {
    // Projection and View matrices
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)

    // 3D text object
    private lateinit var text3D: Text3D
    private var currentText: String = "Default"

    // Rotation angles
    private var angleX = 0.0f
    private var angleY = 0.0f

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // Initialize the text object
        text3D = Text3D(context, currentText)

        // Enable depth testing for 3D effects
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
    }

    override fun onDrawFrame(gl: GL10?) {
        // Clear the color and depth buffer
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -5f, 0f, 0f, 0f, 0f, 1f, 0f)

        // Apply rotation to the model matrix
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.rotateM(modelMatrix, 0, angleX, 1.0f, 0.0f, 0.0f)
        Matrix.rotateM(modelMatrix, 0, angleY, 0.0f, 1.0f, 0.0f)

        // Multiply the matrices: projection * view * model
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0)

        // Draw the text
        text3D.draw(mvpMatrix)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()

        // Create a projection matrix
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    // Setters for rotation
    fun setRotation(angleX: Float, angleY: Float) {
        this.angleX = angleX
        this.angleY = angleY
    }

    // Set new text for rendering
    fun setText(text: String) {
        currentText = text
        text3D.setText(currentText)  // Update the text in the text object
    }

    fun getCurrentXRotation(): Float = angleX
    fun getCurrentYRotation(): Float = angleY
}

