package com.dicoding.picodiploma.storyappsubmission.userinterface.addingstoryofmine

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.storyappsubmission.R
import com.dicoding.picodiploma.storyappsubmission.data.modeldata.LoginDataClass
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.ResponseOfResult
import com.dicoding.picodiploma.storyappsubmission.databinding.ActivityAddStoryOfMineBinding
import com.dicoding.picodiploma.storyappsubmission.userinterface.addingstoryofmine.utils.UtilsInThisStoryApp.fromUriToFile
import com.dicoding.picodiploma.storyappsubmission.userinterface.addingstoryofmine.utils.UtilsInThisStoryApp.reduceFileImageOfMyStoryApp
import com.dicoding.picodiploma.storyappsubmission.userinterface.addingstoryofmine.utils.UtilsInThisStoryApp.rotateBitmapImageOfMyStoryApp
import com.dicoding.picodiploma.storyappsubmission.userinterface.settingofmystoryapp.SettingAppUser
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.FactoryOfViewModel
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.ViewModelOfAddStory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryOfMine : AppCompatActivity() {
    private lateinit var myStoryBinding: ActivityAddStoryOfMineBinding
    private lateinit var loginDataClass: LoginDataClass
    private var getFile: File? = null
    private var result: Bitmap? = null
    private var location: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val viewModelOfAddStory: ViewModelOfAddStory by viewModels {
        FactoryOfViewModel.getInstance(this)
    }

    override fun onRequestPermissionsResult(
        requestCodeOfMyStoryApp: Int,
        permissionsOfMyStoryApp: Array<out String>,
        grantResultsOfMyStoryApp: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCodeOfMyStoryApp,
            permissionsOfMyStoryApp,
            grantResultsOfMyStoryApp
        )
        if (requestCodeOfMyStoryApp == PERMISSION_OF_REQUEST_CODE) {
            if (!allPermissionIsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.permission_is_no_granted),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionIsGranted() = REQUIRED_PERMISSION_OF_MY_STORY_APP.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myStoryBinding = ActivityAddStoryOfMineBinding.inflate(layoutInflater)
        setContentView(myStoryBinding.root)
        supportActionBar!!.hide()

        if (!allPermissionIsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSION_OF_MY_STORY_APP,
                PERMISSION_OF_REQUEST_CODE
            )
        }
        bindingButtonOfMyStoryApp()
        user()

    }

    private fun bindingButtonOfMyStoryApp() {
        myStoryBinding.apply {
            btnSetting.setOnClickListener {
                val view = Intent(this@AddStoryOfMine, SettingAppUser::class.java)
                startActivity(view)

            }
            buttonCamera.setOnClickListener { startCameraXofMyStoryApp() }
            buttonGallery.setOnClickListener { addPhotoOfMyStoryApp() }
            uploadAStory.setOnClickListener { lifecycleScope.launch(Dispatchers.Main) { uploadImageOfMyStoryApp() } }

        }
    }

    private fun user() {
        loginDataClass = intent.getParcelableExtra(EXTRA_USER)!!
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun addPhotoOfMyStoryApp() {
        val intentPhotoOfMyStoryApp = Intent()
        intentPhotoOfMyStoryApp.action = Intent.ACTION_GET_CONTENT
        intentPhotoOfMyStoryApp.type = "image/*"
        val photoChooser = Intent.createChooser(intentPhotoOfMyStoryApp, "Choose a Picture")
        galleryLauncher.launch(photoChooser)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImageOfMyStoryApp: Uri = result.data?.data as Uri
                val fileImageOfMyStoryApp =
                    fromUriToFile(selectedImageOfMyStoryApp, this@AddStoryOfMine)
                getFile = fileImageOfMyStoryApp

                myStoryBinding.photoOfUser.setImageURI(selectedImageOfMyStoryApp)
            }
        }

    private fun startCameraXofMyStoryApp() {
        val intentCameraOfMyStoryApp = Intent(this, TheCameraX::class.java)
        cameraLauncher.launch(intentCameraOfMyStoryApp)
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_CODE_OF_MY_CAMERA_X) {
                val fileCameraOfMyStoryApp = it.data?.getSerializableExtra("picture") as File
                val isCameraBack = it.data?.getBooleanExtra("isCameraBack", true) as Boolean

                getFile = fileCameraOfMyStoryApp
                result = rotateBitmapImageOfMyStoryApp(
                    BitmapFactory.decodeFile(getFile?.path),
                    isCameraBack
                )
                myStoryBinding.photoOfUser.setImageBitmap(result)
            }
        }

    private fun uploadImageOfMyStoryApp() {
        when {

            getFile != null -> {
                val fileImageOfMyStoryApp = reduceFileImageOfMyStoryApp(getFile as File)
                val descriptionTextOfMyStoryApp = myStoryBinding.descriptionEdit.text.toString()
                    .toRequestBody("text/plain".toMediaType())
                val requestImageOfMyStoryApp =
                    fileImageOfMyStoryApp.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val multipartImageOfMyStoryApp: MultipartBody.Part =
                    MultipartBody.Part.createFormData(
                        "photo",
                        fileImageOfMyStoryApp.name,
                        requestImageOfMyStoryApp
                    )
                var lat: RequestBody? = null
                var lon: RequestBody? = null
                if (location != null) {
                    lat = location?.latitude.toString().toRequestBody("text/plain".toMediaType())
                    lon = location?.longitude.toString().toRequestBody("text/plain".toMediaType())
                }

                viewModelOfAddStory.uploadMyStory(
                    loginDataClass.token,
                    descriptionTextOfMyStoryApp,
                    multipartImageOfMyStoryApp,
                    lat,
                    lon
                ).observe(this) { story ->
                    if (story != null) {
                        when (story) {
                            is ResponseOfResult.Loading -> {
                                myStoryBinding.progressBarAddStory.visibility = View.VISIBLE
                            }
                            is ResponseOfResult.Success -> {
                                myStoryBinding.progressBarAddStory.visibility = View.GONE
                                Toast.makeText(
                                    this@AddStoryOfMine,
                                    getString(R.string.success_upload),
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                            is ResponseOfResult.Error -> {
                                myStoryBinding.progressBarAddStory.visibility = View.GONE
                                Toast.makeText(
                                    this@AddStoryOfMine,
                                    getString(R.string.failed_to_upload),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }
                }
            }
            else -> {
                Toast.makeText(
                    this@AddStoryOfMine,
                    getString(R.string.add_warning_image),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    location = it
                    Log.d(TAG, "Lat : ${it.latitude}, Lon : ${it.longitude}")

                } else {
                    Toast.makeText(this, getString(R.string.gps_permission), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            Log.d(TAG, "$it")
            if (it[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                getLocation()
            }
        }

    companion object {
        const val EXTRA_USER = "user"
        const val RESULT_CODE_OF_MY_CAMERA_X = 200
        private val REQUIRED_PERMISSION_OF_MY_STORY_APP = arrayOf(Manifest.permission.CAMERA)
        private const val PERMISSION_OF_REQUEST_CODE = 10
        private const val TAG = "AddStory"
    }
}