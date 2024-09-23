// Text3D.kt
package com.kp.softsavvy.futeres.no_code.renders

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.opengl.GLES20
import android.opengl.GLUtils
import android.opengl.Matrix
import android.util.Log
import com.kp.softsavvy.futeres.no_code.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
class Text3D(private val context: Context, initialText: String) {
    private var text = initialText

    // OpenGL variables
    private var textureId: Int = -1
    private lateinit var vertexBuffer: FloatBuffer
    private lateinit var texCoordBuffer: FloatBuffer
    private var program: Int = -1

    init {
        setupBuffers()
        loadTextTexture(text)
        setupShaders()
    }

    // Update the text and reload the texture
    fun setText(newText: String) {
        text = newText
        loadTextTexture(text)
    }

    // Draw the 3D text
    fun draw(mvpMatrix: FloatArray) {
        GLES20.glUseProgram(program)

        // Pass in the position information
        val positionHandle = GLES20.glGetAttribLocation(program, "aPosition")
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer)

        // Pass in the texture coordinate information
        val texCoordHandle = GLES20.glGetAttribLocation(program, "aTexCoord")
        GLES20.glEnableVertexAttribArray(texCoordHandle)
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 0, texCoordBuffer)

        // Set the active texture and bind the texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)

        // Pass the MVP matrix to the shader
        val mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)

        // Draw the quad (text)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        // Disable the attributes
        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(texCoordHandle)
    }

    // Create the buffers for the vertex and texture coordinates
    private fun setupBuffers() {
        // Vertex positions for a quad (X, Y, Z)
        val vertices = floatArrayOf(
            -1.0f,  1.0f, 0.0f,   // Top-left
            -1.0f, -1.0f, 0.0f,   // Bottom-left
            1.0f,  1.0f, 0.0f,   // Top-right
            1.0f, -1.0f, 0.0f    // Bottom-right
        )

        // Texture coordinates (S, T)
        val texCoords = floatArrayOf(
            0.0f, 0.0f,  // Top-left
            0.0f, 1.0f,  // Bottom-left
            1.0f, 0.0f,  // Top-right
            1.0f, 1.0f   // Bottom-right
        )

        // Allocate buffers for vertices and texture coordinates
        vertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        vertexBuffer.put(vertices).position(0)

        texCoordBuffer = ByteBuffer.allocateDirect(texCoords.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        texCoordBuffer.put(texCoords).position(0)
    }

    // Load the text into a texture
    private fun loadTextTexture(text: String) {
        val bitmap = createTextBitmap(text)
        Log.e("TAG", "loadTextTexture:" , )
        // Generate a texture ID if it doesn't exist
        if (textureId == -1) {
            val textureIds = IntArray(1)
            GLES20.glGenTextures(1, textureIds, 0)
            textureId = textureIds[0]
        }

        // Bind the texture and set texture parameters
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

        // Load the bitmap into the texture
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap.recycle() // Recycle the bitmap as it is no longer needed
    }

    // Create a bitmap from the input text
    private fun createTextBitmap(text: String): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = 100f
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.LEFT

        val textWidth = paint.measureText(text).toInt()
        val textHeight = (paint.descent() - paint.ascent()).toInt()

        // Create a bitmap to hold the text
        val bitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawText(text, 0f, -paint.ascent(), paint)

        return bitmap
    }

    // Setup shaders for text rendering
    private fun setupShaders() {
        val vertexShaderCode = """
            uniform mat4 uMVPMatrix;
            attribute vec4 aPosition;
            attribute vec2 aTexCoord;
            varying vec2 vTexCoord;
            void main() {
                gl_Position = uMVPMatrix * aPosition;
                vTexCoord = aTexCoord;
            }
        """.trimIndent()

        val fragmentShaderCode = """
            precision mediump float;
            uniform sampler2D uTexture;
            varying vec2 vTexCoord;
            void main() {
                gl_FragColor = texture2D(uTexture, vTexCoord);
            }
        """.trimIndent()

        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // Create a program and link the shaders
        program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)
    }

    // Helper method to load a shader
    private fun loadShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }
}

