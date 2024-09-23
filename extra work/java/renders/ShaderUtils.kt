// ShaderUtils.kt
package com.kp.softsavvy.futeres.no_code.renders

import android.content.Context
import android.opengl.GLES20
import java.io.BufferedReader
import java.io.InputStreamReader

//object ShaderUtils {
//    fun loadFromRawResource(context: Context, resId: Int): String {
//        val inputStream = context.resources.openRawResource(resId)
//        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
//        return bufferedReader.use { it.readText() }
//    }
//
//    fun compileShader(type: Int, shaderCode: String): Int {
//        val shader = GLES20.glCreateShader(type)
//        GLES20.glShaderSource(shader, shaderCode)
//        GLES20.glCompileShader(shader)
//        return shader
//    }
//
//    fun createProgram(vertexShader: Int, fragmentShader: Int): Int {
//        val program = GLES20.glCreateProgram()
//        GLES20.glAttachShader(program, vertexShader)
//        GLES20.glAttachShader(program, fragmentShader)
//        GLES20.glLinkProgram(program)
//        return program
//    }
//}
