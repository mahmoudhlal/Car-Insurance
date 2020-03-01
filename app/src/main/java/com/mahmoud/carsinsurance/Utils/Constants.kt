package com.mahmoud.carsinsurance.Utils

import com.mahmoud.carsinsurance.R

class Constants {
    companion object {
        const val GALLERY = 3
        const val CAMERA = 2
        const val permission_camera_code = 786
        const val permission_write_data = 788
        const val permission_read_data = 789
        const val Select_image_from_gallery_code = 3
        const val REQUEST_VIDEO_CAPTURE = 109
        const val PICKFILE_REQUEST_CODE = 141
        const val SELECT_VIDEO = 110
        const val PERMISSION_ACCESS_LOCATION = 111
        const val RECORD_AUDIO_REQUEST_CODE = 101
        var IS_VIDEO = false
        const val USER = "user"
        const val ADMIN = "admin"
        const val COMPANY = "company"
        var CURRENT_ROLE: String? = null
        var IS_FILE = false
        var offers = arrayListOf("Best offers","pay 1020 per month","save your car",
            "best offers","pay 1020 per month","Save your car")
        var collers = arrayOf(R.color.colorAccent,R.color.colorAccent,R.color.colorAccent,R.color.colorAccent)
    }
}