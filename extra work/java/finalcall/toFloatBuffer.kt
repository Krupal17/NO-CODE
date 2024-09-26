package com.kp.softsavvy.futeres.no_code.finalcall

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

fun FloatArray.toFloatBuffer(): FloatBuffer {
    val buffer = ByteBuffer.allocateDirect(this.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
    buffer.put(this).position(0)
    return buffer
}

fun ShortArray.toShortBuffer(): ShortBuffer {
    val buffer = ByteBuffer.allocateDirect(this.size * 2)
        .order(ByteOrder.nativeOrder())
        .asShortBuffer()
    buffer.put(this).position(0)
    return buffer
}
