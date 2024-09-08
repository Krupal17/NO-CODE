package com.kp.softsavvy.futeres.no_code.renders

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.kp.softsavvy.futeres.no_code.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var renderer: Text3DRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        // Initialize the renderer
        renderer = Text3DRenderer(this)

        // Initialize the GLSurfaceView and set it up
        binding.apply {
            // Set OpenGL ES 2.0 context
            glSurfaceView.setEGLContextClientVersion(2)

            // Set the renderer for the GLSurfaceView
            glSurfaceView.setRenderer(renderer)

            // Set the render mode to only render when requested
            glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

            // Listener for the "Apply Text" button
            applyButton.setOnClickListener {
                val inputText = editText.text.toString()
                renderer.setText(inputText)  // Pass the text to the renderer
                glSurfaceView.requestRender()  // Re-render to display the new text
            }

            // Listener for X-axis rotation (horizontal rotation)
            seekBarX.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    // Set X-axis rotation based on seek bar progress
                    renderer.setRotation(progress.toFloat(), renderer.getCurrentYRotation())
                    glSurfaceView.requestRender() // Request to re-render with the new rotation
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            // Listener for Y-axis rotation (vertical rotation)
            seekBarY.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    // Set Y-axis rotation based on seek bar progress
                    renderer.setRotation(renderer.getCurrentXRotation(), progress.toFloat())
                    glSurfaceView.requestRender()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }
}