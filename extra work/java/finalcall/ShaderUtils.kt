package com.kp.softsavvy.futeres.no_code.finalcall

import android.opengl.GLES20
import android.util.Log

object ShaderUtils {

    private const val TAG = "ShaderUtils"

    // Compile a shader from source code
    fun compileShader(type: Int, shaderCode: String): Int {
        // Create a new shader object
        val shader = GLES20.glCreateShader(type)

        // Pass the shader source code to OpenGL
        GLES20.glShaderSource(shader, shaderCode)

        // Compile the shader
        GLES20.glCompileShader(shader)

        // Check for compile errors
        val compileStatus = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
        if (compileStatus[0] == 0) {
            Log.e(TAG, "Shader compile failed: ${GLES20.glGetShaderInfoLog(shader)}")
            GLES20.glDeleteShader(shader)
            throw RuntimeException("Shader compile failed")
        }

        return shader
    }

    // Link vertex and fragment shaders into a program
    fun createProgram(vertexShaderCode: String, fragmentShaderCode: String): Int {
        // Compile both shaders
        val vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // Create a new OpenGL program
        val program = GLES20.glCreateProgram()

        // Attach the shaders to the program
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)

        // Link the program
        GLES20.glLinkProgram(program)

        // Check for linking errors
        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] == 0) {
            Log.e(TAG, "Program linking failed: ${GLES20.glGetProgramInfoLog(program)}")
            GLES20.glDeleteProgram(program)
            throw RuntimeException("Program linking failed")
        }

        return program
    }
}
