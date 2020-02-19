package com.mahmoud.carsinsurance.Utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class Permissions {

    private val context: Context? = null


    companion object {
        private var Instance: Permissions? = null
        fun getInstance(): Permissions? {
            if (Instance == null) Instance = Permissions()
            return Instance
        }
    }
    fun check_cameraPermission(
        context: Activity?,
        fragment: Fragment?
    ): Boolean {
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED /*&&
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED*/) {
            return true
        } else {
            if (fragment != null) fragment.requestPermissions(
                arrayOf(Manifest.permission.CAMERA /*, Manifest.permission.WRITE_EXTERNAL_STORAGE*/),
                Constants.permission_camera_code
            ) else ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.CAMERA /*, Manifest.permission.WRITE_EXTERNAL_STORAGE*/),
                Constants.permission_camera_code
            )
        }
        return false
    }

    fun check_ReadStoragePermission(
        context: Activity,
        fragment: Fragment?
    ): Boolean {
        if (ContextCompat.checkSelfPermission(
                context.applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            try {
                if (fragment != null) fragment.requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.permission_read_data
                ) else ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.permission_read_data
                )
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
        return false
    }

    fun check_writeStoragePermission(
        context: Activity,
        fragment: Fragment?
    ): Boolean {
        if (ContextCompat.checkSelfPermission(
                context.applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            try {
                if (fragment != null) fragment.requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Constants.permission_write_data
                ) else ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Constants.permission_write_data
                )
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
        return false
    }

    fun check_Location_Permission(
        context: Activity?,
        fragment: Fragment?
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            } else { //  PERMISSION_DENIED
                if (fragment != null) fragment.requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), Constants.PERMISSION_ACCESS_LOCATION
                ) else ActivityCompat.requestPermissions(
                    context, arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), Constants.PERMISSION_ACCESS_LOCATION
                )
            }
        } else {
            return true
        }
        return false
    }

    fun checkPermissionToRecordAudio(
        context: Activity?,
        fragment: Fragment?
    ): Boolean { // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid checking the build version since Context.checkSelfPermission(...) is only available in Marshmallow
// 2) Always check for permission (even if permission has already been granted) since the user can revoke permissions at any time through Settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.RECORD_AUDIO
                )
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                        == PackageManager.PERMISSION_GRANTED)
            ) {
                return true
            } else {
                if (fragment != null) fragment.requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    Constants.RECORD_AUDIO_REQUEST_CODE
                ) else ActivityCompat.requestPermissions(
                    context, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    Constants.RECORD_AUDIO_REQUEST_CODE
                )
            }
        } else {
            return true
        }
        return false
    }


}