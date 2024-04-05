package com.dicoding.picodiploma.storyappsubmission.userinterface.addingstoryofmine.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import com.dicoding.picodiploma.storyappsubmission.R
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object UtilsInThisStoryApp {
    private const val FILENAME_FORMAT_OF_MY_STORY_APP = "dd-MMM-yyyy"

    val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT_OF_MY_STORY_APP,
        Locale.US
    ).format(System.currentTimeMillis())

    fun createCustomTemplateOfMyStoryAppFile(context: Context): File {
        val storage: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storage)
    }

    fun createFile(application: Application): File {
        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        val outputDirectory = if (
            mediaDir != null && mediaDir.exists()
        ) mediaDir else application.filesDir

        return File(outputDirectory, "$timeStamp.jpg")
    }

    fun rotateBitmapImageOfMyStoryApp(bitmap: Bitmap, isCameraBack: Boolean = false): Bitmap {
        val matrix = Matrix()
        return if (isCameraBack) {
            matrix.postRotate(90f)
            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        } else {
            matrix.postRotate(-90f)
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        }
    }

    fun fromUriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val theStoryFileOfMine = createCustomTemplateOfMyStoryAppFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(theStoryFileOfMine)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return theStoryFileOfMine
    }

    fun reduceFileImageOfMyStoryApp(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQualityOfMyStoryApp = 100
        var streamLengthOfMyStoryApp: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQualityOfMyStoryApp, bmpStream)
            val bmpByteArrayPic = bmpStream.toByteArray()
            streamLengthOfMyStoryApp = bmpByteArrayPic.size
            compressQualityOfMyStoryApp -= 5
        } while (streamLengthOfMyStoryApp > 1000000)
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            compressQualityOfMyStoryApp,
            FileOutputStream(file)
        )
        return file
    }
}
