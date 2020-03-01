package com.mahmoud.carsinsurance.Utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.mahmoud.carsinsurance.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class TakePhotoUtils {
    var mCurrentPhotoPath: String? = null
    private var bitmap: Bitmap? = null


    companion object {
        private var Instance: TakePhotoUtils? = null
        fun getInstance(): TakePhotoUtils? {
            if (Instance == null) Instance = TakePhotoUtils()
            return Instance
        }
    }

    //region PHOTO
    fun SelectPhotoDialog(
        activity: Activity,
        fragment: Fragment?
    ) {
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.view_take_photo)
        val takePhoto = dialog.findViewById<LinearLayout>(R.id.takephoto)
        val openGallery = dialog.findViewById<LinearLayout>(R.id.opengallery)
        dialog.window!!.attributes
            .windowAnimations = R.style.alert_dialog
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        takePhoto.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Permissions.getInstance()!!.check_cameraPermission(activity, fragment)) {
                    ChooseImageCamera(activity, fragment)
                }
            } else {
                ChooseImageCamera(activity, fragment)
            }
            dialog.dismiss()
        }
        openGallery.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Permissions.getInstance()!!.check_ReadStoragePermission(activity, fragment)) {
                    ChooseImageGallery(activity, fragment)
                }
            } else {
                ChooseImageGallery(activity, fragment)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    fun ChooseImageCamera(
        context: Activity,
        fragment: Fragment?
    ) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(context.packageManager) != null) { // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile(context)
            } catch (ex: IOException) { // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    context,
                    "com.mahmoud.printinghouse.fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                if (fragment != null) fragment.startActivityForResult(
                    takePictureIntent,
                    Constants.CAMERA
                ) else context.startActivityForResult(takePictureIntent, Constants.CAMERA)
            }
        }
    }

    fun ChooseImageGallery(
        context: Activity,
        fragment: Fragment?
    ) {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (fragment != null) fragment.startActivityForResult(
            Intent.createChooser(
                intent,
                context.resources.getString(R.string.choose_photo)
            ), Constants.GALLERY
        ) else context.startActivityForResult(
            Intent.createChooser(intent, context.resources.getString(R.string.choose_photo)),
            Constants.GALLERY
        )
    }

    @Throws(IOException::class)
    private fun createImageFile(context: Context): File? { // Create an image file name
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                .format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun getImagePathFromInputStreamUri(
        context: Context,
        uri: Uri
    ): String? {
        var inputStream: InputStream? = null
        var filePath: String? = null
        if (uri.authority != null) {
            try {
                inputStream = context.contentResolver.openInputStream(uri) // context needed
                val photoFile = createTemporalFileFrom(inputStream, context)
                filePath = photoFile!!.path
            } catch (e: IOException) { // log
            } finally {
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return filePath
    }

    @Throws(IOException::class)
    private fun createTemporalFileFrom(
        inputStream: InputStream?,
        context: Context
    ): File? {
        var targetFile: File? = null
        if (inputStream != null) {
            var read: Int
            val buffer = ByteArray(8 * 1024)
            targetFile = createTemporalFile(context)
            val outputStream: OutputStream = FileOutputStream(targetFile)
            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
            outputStream.flush()
            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return targetFile
    }

    private fun createTemporalFile(context: Context): File? {
        return File(context.externalCacheDir, "tempFile.jpg") // context needed
    }

    @Throws(IOException::class)
    private fun fixRotate(photoPath: String?) {
        val ei = ExifInterface(photoPath!!)
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> bitmap = rotateImage(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> bitmap = rotateImage(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> bitmap = rotateImage(270f)
            ExifInterface.ORIENTATION_NORMAL -> {
            }
            else -> {
            }
        }
    }

    private fun rotateImage(angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            bitmap!!,
            0,
            0,
            bitmap!!.width,
            bitmap!!.height,
            matrix,
            true
        )
    }


    //endregion

    //endregion
    fun setImage(
        type: Int,
        contentURI: Uri,
        context: Context,
        imageView: ImageView
    ): Bitmap? {
        try {
            if (type == Constants.GALLERY) {
                bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, contentURI)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, contentURI)
                }
                //bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, contentURI)
                mCurrentPhotoPath = getImagePathFromInputStreamUri(context, contentURI)
            } else bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, BitmapFactory.Options())
            fixRotate(mCurrentPhotoPath)
            imageView.setPadding(0, 0, 0, 0)
            imageView.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    fun getBitmap(
        type: Int,
        contentURI: Uri,
        context: Context
    ): Bitmap? {
        try {
            if (type == Constants.GALLERY) {
                bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, contentURI)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, contentURI)
                }

                //bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, contentURI)
                mCurrentPhotoPath = getImagePathFromInputStreamUri(context, contentURI)
            } else bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, BitmapFactory.Options())
            fixRotate(mCurrentPhotoPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    fun getMultiPartFromBitmap(
        mBitmap: Bitmap,
        context: Context,
        fileName: String
    ): MultipartBody.Part? {
        val filesDir = context.applicationContext.filesDir
        val file = File(filesDir, "$fileName.jpg")
        val os: OutputStream
        try {
            os = FileOutputStream(file)
            Log.i("sadsad", mBitmap.byteCount.toString() + "")
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 30, os)
            os.flush()
            os.close()
        } catch (e: Exception) {
            Toast.makeText(context.applicationContext, "select photo", Toast.LENGTH_SHORT)
                .show()
            Log.e("Error writing bitmap", e.message!!)
        }
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        return MultipartBody.Part.createFormData(fileName, file.name, requestFile)
    }

}