package com.kp.softsavvy.futeres.no_code.false_coding

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kp.softsavvy.futeres.no_code.R
import com.kp.softsavvy.futeres.no_code.databinding.ActivityArtisticBinding

class ArtisticActivity : AppCompatActivity() {

    val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            // Apply texture to the new sticker
            getBitmapFromUri(contentResolver, it)?.let {
                selectedSticker?.setTexture(it)
            }
        }
    }

    val binding: ActivityArtisticBinding by lazy {
        ActivityArtisticBinding.inflate(layoutInflater)
    }
    private lateinit var stickerManager: TextStickerManager
    private var currentScale = 1f // default scale
    private var currentXRotation = 0f // default X rotation
    private var currentYRotation = 0f // default Y rotation
    private var selectedSticker: TextView3D? = null // To keep track of the current sticker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initview()
    }

    private fun initview() {
        stickerManager = TextStickerManager(binding.stickerContainer)

        binding.apply {

            addStickerBtn.setOnClickListener {
                val text = editText.text.toString()
                val sticker = stickerManager.addSticker(text)
                selectedSticker = sticker // Keep track of the selected sticker
                selectedSticker?.let {
                    setupStickerInteractions(it)
//                    pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }

            // Set up SeekBar for X-axis rotation
            seekBarX.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                    currentXRotation = progress.toFloat()
                    selectedSticker?.let {
                        applyTransformations(it, currentScale, currentXRotation, currentYRotation)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            // Set up SeekBar for Y-axis rotation
            seekBarY.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                    currentYRotation = progress.toFloat()
                    selectedSticker?.let {
                        applyTransformations(it, currentScale, currentXRotation, currentYRotation)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            // Set up SeekBar for Scale
            seekBarSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                    val minValue = 1.0f
                    val maxValue = 2.0f
                    val progressPercentage = progress / 100f
                    val value = minValue + (progressPercentage * (maxValue - minValue))

                    currentScale = value
                    selectedSticker?.let {
                        applyTransformations(it, currentScale, currentXRotation, currentYRotation)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }


    //Text Stickers
    private fun applyTransformations(
        sticker: TextView3D, scale: Float, xRotation: Float, yRotation: Float
    ) {
        sticker.scaleX = scale
        sticker.scaleY = scale
        sticker.rotationX = xRotation // Apply X-axis rotation
        sticker.rotationY = yRotation // Apply Y-axis rotation

        // Optional: Adjust shadow based on the rotation
//        sticker.setShadowLayer(10f, 5f, 5f, Color.BLACK)
    }

    fun setupStickerInteractions(sticker: TextView3D) {
        sticker.setOnTouchListener(object : View.OnTouchListener {
            var downX = 0f
            var downY = 0f
            var dX = 0f
            var dY = 0f

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                event?.let {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            downX = it.rawX
                            downY = it.rawY
                            dX = v!!.x - downX
                            dY = v.y - downY
                            // Call performClick() to handle accessibility events
                            v.performClick()
                            selectedSticker = sticker
                        }

                        MotionEvent.ACTION_MOVE -> {
                            v?.animate()?.x(it.rawX + dX)?.y(it.rawY + dY)?.setDuration(0)?.start()
                        }

                        else -> {}
                    }
                }
                return true
            }
        })

    }
}