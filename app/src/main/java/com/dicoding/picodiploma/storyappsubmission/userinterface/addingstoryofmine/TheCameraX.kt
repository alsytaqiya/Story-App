package com.dicoding.picodiploma.storyappsubmission.userinterface.addingstoryofmine

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.storyappsubmission.R
import com.dicoding.picodiploma.storyappsubmission.databinding.ActivityTheCameraXBinding
import com.dicoding.picodiploma.storyappsubmission.userinterface.addingstoryofmine.utils.UtilsInThisStoryApp

class TheCameraX : AppCompatActivity() {

    private lateinit var binding: ActivityTheCameraXBinding

    private var captureImageOnMyStoryApp: ImageCapture? = null
    private var cameraOfMyStoryApp: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTheCameraXBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindingCamera()
    }

    private fun bindingCamera() {
        binding.apply {
            captureImageMyStoryApp.setOnClickListener {
                addPhotoOfMyStoryApp()
            }

            switchCameraMyStoryApp.setOnClickListener {
                cameraOfMyStoryApp =
                    if (cameraOfMyStoryApp == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                    else CameraSelector.DEFAULT_BACK_CAMERA
                cameraStartStory()
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        cameraStartStory()
    }


    private fun addPhotoOfMyStoryApp() {
        val imageCaptureOfMyStoryApp = captureImageOnMyStoryApp ?: return
        val filePhotoOfMyStoryApp = UtilsInThisStoryApp.createFile(application)

        val outputOptionsStreamOfMyStoryApp =
            ImageCapture.OutputFileOptions.Builder(filePhotoOfMyStoryApp).build()
        imageCaptureOfMyStoryApp.takePicture(
            outputOptionsStreamOfMyStoryApp, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val intentCameraOfMyStoryApp = Intent()
                    intentCameraOfMyStoryApp.putExtra("picture", filePhotoOfMyStoryApp)
                    intentCameraOfMyStoryApp.putExtra(
                        "isCameraBack",
                        cameraOfMyStoryApp == CameraSelector.DEFAULT_BACK_CAMERA
                    )
                    setResult(AddStoryOfMine.RESULT_CODE_OF_MY_CAMERA_X, intentCameraOfMyStoryApp)
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        this@TheCameraX,
                        getString(R.string.failed_pic),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        )
    }


    private fun cameraStartStory() {
        val cameraProvider = ProcessCameraProvider.getInstance(this)

        cameraProvider.addListener({
            val providerCamera: ProcessCameraProvider = cameraProvider.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinderMyStoryAppCamera.surfaceProvider)
                }

            captureImageOnMyStoryApp = ImageCapture.Builder().build()

            try {
                providerCamera.unbindAll()
                providerCamera.bindToLifecycle(
                    this,
                    cameraOfMyStoryApp,
                    preview,
                    captureImageOnMyStoryApp
                )
            } catch (e: Exception) {
                Toast.makeText(
                    this@TheCameraX,
                    getString(R.string.failed_camera),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}