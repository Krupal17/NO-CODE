package com.kp.softsavvy.futeres.no_code.unityTry

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.kp.softsavvy.futeres.no_code.R
import com.unity3d.player.UnityPlayer

class MainActivity : AppCompatActivity() {
    private var mUnityPlayer: UnityPlayer? = null
    private var xSeekBar: SeekBar? = null
    private var ySeekBar: SeekBar? = null
    private var zSeekBar: SeekBar? = null
    private var btnDone: Button? = null
    private var edtText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set your custom layout
        setContentView(R.layout.activity_main)

        // Initialize Unity Player
        mUnityPlayer = UnityPlayer(this)
        // Add the Unity view to your layout
        val unityView = mUnityPlayer!!.view
        val unityFrame = findViewById<FrameLayout>(R.id.unityFrame)
        unityFrame.addView(unityView)

        // Initialize SeekBars
        xSeekBar = findViewById(R.id.xSeekBar)
        ySeekBar = findViewById(R.id.ySeekBar)
        zSeekBar = findViewById(R.id.zSeekBar)
        btnDone = findViewById(R.id.btnDone)
        edtText = findViewById(R.id.edtText)

        // Set SeekBar listeners
        xSeekBar?.setOnSeekBarChangeListener(seekBarChangeListener)
        ySeekBar?.setOnSeekBarChangeListener(seekBarChangeListener)
        zSeekBar?.setOnSeekBarChangeListener(seekBarChangeListener)

        btnDone?.setOnClickListener(View.OnClickListener { view: View? ->
            if (edtText?.text?.isNotEmpty() == true) {
                setTextContent(edtText?.text.toString())
            }
        })
    }

    fun setTextContent(text: String?) {
        UnityPlayer.UnitySendMessage("Text3DObject", "SetTextContent", text)
    }

    private val seekBarChangeListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            val value = progress.toString()

            if (seekBar === xSeekBar) {
                UnityPlayer.UnitySendMessage("Text3DObject", "SetXRotation", value)
            } else if (seekBar === ySeekBar) {
                UnityPlayer.UnitySendMessage("Text3DObject", "SetYRotation", value)
            } else if (seekBar === zSeekBar) {
                UnityPlayer.UnitySendMessage("Text3DObject", "SetZRotation", value)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            // Optional
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            // Optional
        }
    }

    override fun onDestroy() {
        mUnityPlayer!!.destroy()
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mUnityPlayer!!.pause()
    }

    override fun onResume() {
        super.onResume()
        mUnityPlayer!!.resume()
    }
}
