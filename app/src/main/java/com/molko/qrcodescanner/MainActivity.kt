package com.molko.qrcodescanner

import android.Manifest
import android.content.DialogInterface
import android.content.DialogInterface.OnDismissListener
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.objdetect.QRCodeDetector

class MainActivity : AppCompatActivity(), CvCameraViewListener2 {
    lateinit var mPermissions: Array<String>
    private val mPermissionsReturnCode = 10
    
    lateinit var mRgba: Mat
    
    private var mDetect: Boolean = false
    lateinit var mDetector: QRCodeDetector
    
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
                longToast(getString(R.string.permissions))
            }
        }
    }
    
    private fun checkPermissions() {
        var permissionsGranted = true
        for (permission in mPermissions) {
            if (ContextCompat.checkSelfPermission(this@MainActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = false
                break
            }
        }
        if (permissionsGranted) {
            init()
        }
        else {
            ActivityCompat.requestPermissions(this@MainActivity, mPermissions, mPermissionsReturnCode)
        }
    }
    
    private fun init() {
        viewCamera.setCvCameraViewListener(this@MainActivity)
        fabHistory.setOnClickListener {
            startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
        }
        mDetector = QRCodeDetector()
        mDetect = true
        viewCamera.enableView()
    }
    
    inner class QRDialogDismissListener : OnDismissListener {
        override fun onDismiss(dialog: DialogInterface) {
            mDetect = true
        }
    }
    
    override fun onCameraViewStarted(width: Int, height: Int) {
        mRgba = Mat(height, width, CvType.CV_8UC4)
    }
    
    override fun onCameraViewStopped() { }
    
    override fun onCameraFrame(inputFrame: CvCameraViewFrame): Mat {
        mRgba = inputFrame.rgba()
        if (mDetect) {
            val text = mDetector.detectAndDecode(mRgba)
            if (text.isNotEmpty()) {
                mDetect = false
                runOnUiThread {
                    showQRDialog(this@MainActivity, text, QRDialogDismissListener())
                    doAsync {
                        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, TABLE_NAME).build()
                        db.historyDao().insert(text)
                        db.close()
                    }
                }
            }
        }
        return mRgba
    }
}
