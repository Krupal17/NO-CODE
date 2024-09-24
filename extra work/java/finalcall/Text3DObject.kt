package com.kp.softsavvy.futeres.no_code.finalcall

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Text3DObject() {

    private var program: Int = 0
    private var vertexBuffer: FloatBuffer? = null
    private var indexBuffer: ShortBuffer? = null
    private var vertexCount: Int = 0

    // Rotation angles
    var rotationX = 0f
    var rotationY = 0f
    var rotationZ = 0f

    // Position of the text
    var positionX = 0f
    var positionY = 0f
    var positionZ = -5f // Default distance from the camera

    // Scale of the text
    var scale = 1f

    // Text that will be rendered
    var text: String = "<"

    // Color of the text
    var textColor = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f) // White

    // Shader code remains the same as before
    private val vertexShaderCode = """
        uniform mat4 uMVPMatrix;
        attribute vec4 vPosition;
        void main() {
            gl_Position = uMVPMatrix * vPosition;
        }
    """.trimIndent()

    private val fragmentShaderCode = """
        precision mediump float;
        uniform vec4 vColor;
        void main() {
            gl_FragColor = vColor;
        }
    """.trimIndent()

    init {
        program = ShaderUtils.createProgram(vertexShaderCode, fragmentShaderCode)
    }

    // Update the text color
    fun setTextColor(r: Float, g: Float, b: Float, a: Float) {
        textColor = floatArrayOf(r, g, b, a)
    }

    // Update the text size
    fun setTextSize(size: Float) {
        val bitmap = textToBitmap(text, size)
        generate3DTextGeometry(bitmap)
    }

    private fun generate3DTextGeometry(bitmap: Bitmap) {
        val vertices = bitmapTo3DVertices(bitmap)
        vertexBuffer = vertices.toFloatBuffer()
        val indices = generateIndices(vertices.size / 3)
        indexBuffer = indices.toShortBuffer()
        vertexCount = indices.size
    }

    private fun textToBitmap(text: String, size: Float): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = size // Adjustable text size
        paint.color = 0xFFFFFFFF.toInt()
        paint.textAlign = Paint.Align.LEFT

        val baseline = -paint.ascent()
        val width = (paint.measureText(text) + 0.5f).toInt()
        val height = (baseline + paint.descent() + 0.5f).toInt()

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawText(text, 0f, baseline, paint)

        return bitmap
    }

    // Draw function to render the 3D text
    // Draw function to render the 3D text
    fun draw(viewMatrix: FloatArray, projectionMatrix: FloatArray) {
        GLES20.glUseProgram(program)

        // Apply transformations
        val modelMatrix = FloatArray(16)
        Matrix.setIdentityM(modelMatrix, 0)

        // Apply rotations in a specific order
        Matrix.rotateM(modelMatrix, 0, rotationZ, 0f, 0f, 1f) // Rotate around Z first
        Matrix.rotateM(modelMatrix, 0, rotationY, 0f, 1f, 0f) // Then Y
        Matrix.rotateM(modelMatrix, 0, rotationX, 1f, 0f, 0f) // Then X

        // Calculate the Model-View-Projection (MVP) matrix
        val mvpMatrix = FloatArray(16)
        val tempMatrix = FloatArray(16)
        Matrix.multiplyMM(tempMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, tempMatrix, 0)

        // Send the matrices to the shaders
        val mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)

        val colorHandle = GLES20.glGetUniformLocation(program, "vColor")
        GLES20.glUniform4fv(colorHandle, 1, textColor, 0)

        val positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer)

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, vertexCount, GLES20.GL_UNSIGNED_SHORT, indexBuffer)

        GLES20.glDisableVertexAttribArray(positionHandle)
    }



    // Convert bitmap into 3D vertices
    private fun bitmapTo3DVertices(bitmap: Bitmap): FloatArray {
        val width = bitmap.width
        val height = bitmap.height
        val depth = 0.05f // Depth for extrusion

        val vertices = mutableListOf<Float>()

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = bitmap.getPixel(x, y)
                if (pixel != 0) { // If the pixel is not transparent
                    // Front face vertex
                    vertices.add(x.toFloat() / width)    // X position (normalized)
                    vertices.add(y.toFloat() / height)   // Y position (normalized)
                    vertices.add(0f)                      // Z position (front)

                    // Back face vertex (extruded)
                    vertices.add(x.toFloat() / width)    // X position (normalized)
                    vertices.add(y.toFloat() / height)   // Y position (normalized)
                    vertices.add(-depth)                  // Z position (back)
                }
            }
        }

        return vertices.toFloatArray()
    }


    // Generate indices for drawing triangles
    private fun generateIndices(vertexCount: Int): ShortArray {
        val indices = mutableListOf<Short>()

        // Each quad consists of two triangles
        for (i in 0 until vertexCount / 2 step 2) {
            // Front face triangles
            indices.add(i.toShort())              // Top-left
            indices.add((i + 1).toShort())        // Top-right
            indices.add((i + 2).toShort())        // Bottom-left

            indices.add((i + 1).toShort())        // Top-right
            indices.add((i + 3).toShort())        // Bottom-right
            indices.add((i + 2).toShort())        // Bottom-left
        }
        return indices.toShortArray()
    }

}
