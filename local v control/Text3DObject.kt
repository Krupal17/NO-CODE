package com.kp.softsavvy.futeres.no_code.finalcall

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix

class Text3DObject(context: Context) {

    // Program ID for the shaders
    private var program: Int = 0

    // Model-View-Projection Matrix for transformations
    private val mvpMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)

    // Buffers to store vertex data (to be implemented)
    private lateinit var vertexBuffer: FloatArray
    private lateinit var indexBuffer: ShortArray

    // Shader handles
    private var positionHandle: Int = 0
    private var colorHandle: Int = 0
    private var mvpMatrixHandle: Int = 0

    // Rotation variables
    var rotationX = 0f
    var rotationY = 0f
    var rotationZ = 0f

    // Color for the text
    private val textColor = floatArrayOf(0.8f, 0.8f, 0.0f, 1.0f) // Yellow-ish

    // Vertex Shader
    private val vertexShaderCode =
        """
        uniform mat4 uMVPMatrix;
        attribute vec4 vPosition;
        void main() {
            gl_Position = uMVPMatrix * vPosition;
        }
        """.trimIndent()

    // Fragment Shader
    private val fragmentShaderCode =
        """
        precision mediump float;
        uniform vec4 vColor;
        void main() {
            gl_FragColor = vColor;
        }
        """.trimIndent()

    init {
        // Initialize the model matrix
        Matrix.setIdentityM(modelMatrix, 0)

        // Create the program and shaders
        program = ShaderUtils.createProgram(vertexShaderCode, fragmentShaderCode)

        // Prepare vertex data (simple example of a square for text placeholder)
        setupTextData()

        // Initialize handles for the shaders
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        colorHandle = GLES20.glGetUniformLocation(program, "vColor")
        mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")
    }

    // Set up vertex data for the text (currently a placeholder square)
    private fun setupTextData() {
        // Placeholder 3D vertices for a square (you will replace this with 3D text vertices)
        val squareCoords = floatArrayOf(
            -0.5f, 0.5f, 0.0f,   // Top-left
            -0.5f, -0.5f, 0.0f,  // Bottom-left
            0.5f, -0.5f, 0.0f,   // Bottom-right
            0.5f, 0.5f, 0.0f     // Top-right
        )

        // Indices for drawing the square using TRIANGLES
        val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)

        vertexBuffer = squareCoords
        indexBuffer = drawOrder
    }

    fun setRotation(x: Float, y: Float, z: Float) {
        rotationX = x
        rotationY = y
        rotationZ = z
    }

    fun draw(viewMatrix: FloatArray, projectionMatrix: FloatArray) {
        // Use the shader program
        GLES20.glUseProgram(program)

        // Prepare the model matrix (rotate the object)
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.rotateM(modelMatrix, 0, rotationX, 1f, 0f, 0f)
        Matrix.rotateM(modelMatrix, 0, rotationY, 0f, 1f, 0f)
        Matrix.rotateM(modelMatrix, 0, rotationZ, 0f, 0f, 1f)

        // Multiply the model, view, and projection matrices
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0)

        // Pass the MVP matrix to the shader
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)

        // Set the color for drawing
        GLES20.glUniform4fv(colorHandle, 1, textColor, 0)

        // Prepare the vertex data for drawing
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(
            positionHandle, 3, GLES20.GL_FLOAT, false,
            3 * 4, vertexBuffer.toFloatBuffer()
        )

        // Draw the text object (currently a placeholder square)
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES, indexBuffer.size,
            GLES20.GL_UNSIGNED_SHORT, indexBuffer.toShortBuffer()
        )

        // Disable the vertex array
        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}
