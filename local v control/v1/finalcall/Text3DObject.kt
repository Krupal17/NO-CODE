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

    var rotationX = 0f
    var rotationY = 0f
    var rotationZ = 0f

    // Text that will be rendered
    var text: String = "<"

    private val textColor = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f) // White
    // Shader code remains the same as before (vertex & fragment shaders)
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
        generate3DTextGeometry(text)
    }

    // Generate the text geometry from a bitmap, and extrude it to give a 3D effect
    private fun generate3DTextGeometry(text: String) {
        // Convert text to bitmap
        val bitmap = textToBitmap(text)
        val vertices = bitmapTo3DVertices(bitmap)
        vertexBuffer = vertices.toFloatBuffer()

        // Create index buffer for drawing
        val indices = generateIndices(vertices.size / 3)
        indexBuffer = indices.toShortBuffer()
        vertexCount = indices.size
    }

    // Convert text to bitmap
    private fun textToBitmap(text: String): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = 32f // Reduced text size
        paint.color = 0xFFFFFFFF.toInt()
        paint.textAlign = Paint.Align.LEFT

        val baseline = -paint.ascent() // ascent() is negative
        val width = (paint.measureText(text) + 0.5f).toInt() // round
        val height = (baseline + paint.descent() + 0.5f).toInt()

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawText(text, 0f, baseline, paint)

        return bitmap
    }

    // Convert bitmap into 3D vertices

    private fun bitmapTo3DVertices(bitmap: Bitmap): FloatArray {
        val width = bitmap.width
        val height = bitmap.height
        val depth = 0.05f // Reduced depth for extrusion

        val vertices = mutableListOf<Float>()

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = bitmap.getPixel(x, y)
                if (pixel != 0) { // If the pixel is not transparent
                    // Front face
                    vertices.add(x.toFloat() / width)
                    vertices.add(y.toFloat() / height)
                    vertices.add(0f)

                    // Back face (extrusion for 3D)
                    vertices.add(x.toFloat() / width)
                    vertices.add(y.toFloat() / height)
                    vertices.add(-depth)
                }
            }
        }

        return vertices.toFloatArray()
    }

    // Generate indices for drawing triangles
    private fun generateIndices(vertexCount: Int): ShortArray {
        val indices = mutableListOf<Short>()
        for (i in 0 until vertexCount step 4) {
            indices.add(i.toShort())
            indices.add((i + 1).toShort())
            indices.add((i + 2).toShort())
            indices.add(i.toShort())
            indices.add((i + 2).toShort())
            indices.add((i + 3).toShort())
        }
        return indices.toShortArray()
    }

    // Draw function to render the 3D text
    fun draw(viewMatrix: FloatArray, projectionMatrix: FloatArray) {
        GLES20.glUseProgram(program)

        // Apply transformations
        val modelMatrix = FloatArray(16)
        Matrix.setIdentityM(modelMatrix, 0)

        // Apply rotations
        Matrix.rotateM(modelMatrix, 0, rotationX, 1f, 0f, 0f)
        Matrix.rotateM(modelMatrix, 0, rotationY, 0f, 1f, 0f)
        Matrix.rotateM(modelMatrix, 0, rotationZ, 0f, 0f, 1f)

        // Calculate the Model-View-Projection (MVP) matrix
        val mvpMatrix = FloatArray(16)
        val tempMatrix = FloatArray(16)
        Matrix.multiplyMM(tempMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, tempMatrix, 0)
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
}
