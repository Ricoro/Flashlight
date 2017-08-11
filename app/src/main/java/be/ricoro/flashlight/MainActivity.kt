package be.ricoro.flashlight


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.Camera
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    internal var imageButton: ImageButton
    internal var camera: Camera? = null
    internal var parameters: Camera.Parameters
    internal var context: Context? = null
    internal var activity: Activity? = null

    internal var isFlash = false
    internal var isOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(arrayOf(Manifest.permission.CAMERA), 123)
        setContentView(R.layout.activity_main)
        imageButton = findViewById(R.id.imageButton) as ImageButton

        if (applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            camera = Camera.open()
            parameters = camera!!.parameters
            isFlash = true
        }

        imageButton.setOnClickListener {
            if (isFlash) {
                if (!isOn) {
                    imageButton.setImageResource(R.drawable.on)
                    parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
                    camera!!.parameters = parameters
                    camera!!.startPreview()
                    isOn = true
                } else {
                    imageButton.setImageResource(R.drawable.off)
                    parameters.flashMode = Camera.Parameters.FLASH_MODE_OFF
                    camera!!.parameters = parameters
                    camera!!.stopPreview()
                    isOn = false
                }
            } else {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Error...")
                builder.setMessage("Flashlight is not available on this device...")
                builder.setPositiveButton("Ok") { dialogInterface, i ->
                    dialogInterface.dismiss()
                    finish()
                }
                val alertDialog = builder.create()
                alertDialog.show()
            }
        }

    }


    override fun onStop() {
        super.onStop()
        if (camera != null) {
            camera!!.release()
            camera = null
        }
    }

    companion object {
        private val PERMS_REQUEST_CODE = 123
    }


}
