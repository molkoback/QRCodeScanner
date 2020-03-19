package com.molko.qrcodescanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2
import org.opencv.core.Mat

class MainActivity : AppCompatActivity(), CvCameraViewListener2 {
    lateinit var mPermissions: Array<String>
    private val mPermissionsReturnCode = 10
    
    private var mDetect: Boolean = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        
        System.loadLibrary("opencv_java3")

        mPermissions = arrayOf(Manifest.permission.CAMERA)
        checkPermissions()
    }
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == mPermissionsReturnCode) {
            var permissionsGranted = grantResults.isNotEmpty()
            for (res in grantResults) {
                if (res != PackageManager.PERMISSION_GRANTED) {
                    permissionsGranted = false
                }
            }
            if (permissionsGranted) {
                init()
            }
            else {
                Toast.makeText(this, getString(R.string.permissions), Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun checkPermissions() {
        var permissionsGranted = true
        for (permission in mPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = false
                break
            }
        }
        if (permissionsGranted) {
            init()
        }
        else {
            ActivityCompat.requestPermissions(this, mPermissions, mPermissionsReturnCode)
        }
    }
    
    private fun init() {
        viewCamera.setCvCameraViewListener(this)
        fabHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        viewCamera.enableView()
    }
    
    override fun onCameraViewStarted(width: Int, height: Int) { }
    
    override fun onCameraViewStopped() { }
    
    override fun onCameraFrame(inputFrame: CvCameraViewFrame): Mat {
        return inputFrame.rgba()
    }
}
