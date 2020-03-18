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
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc


class MainActivity : AppCompatActivity(), CvCameraViewListener2 {
    lateinit var PERMISSIONS: Array<String>
    private val PERMISSIONS_REQUEST_CODE = 10
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        
        System.loadLibrary("opencv_java3")
        
        PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        checkPermissions()
    }
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            var permissionsGranted = grantResults.isNotEmpty()
            for (res in grantResults) {
                if (res != PackageManager.PERMISSION_GRANTED) {
                    permissionsGranted = false
                }
            }
            if (permissionsGranted) {
                start()
            }
            else {
                Toast.makeText(this, getString(R.string.permissions), Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun checkPermissions() {
        var permissionsGranted = true
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = false
                break
            }
        }
        if (permissionsGranted) {
            start()
        }
        else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_CODE)
        }
    }

    private fun start() {
        viewCamera.setCvCameraViewListener(this)
        viewCamera.enableView()
        fabHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }
    
    override fun onCameraViewStarted(width: Int, height: Int) { }
    
    override fun onCameraViewStopped() { }
    
    override fun onCameraFrame(inputFrame: CvCameraViewFrame): Mat {
        return inputFrame.rgba()
    }
}
