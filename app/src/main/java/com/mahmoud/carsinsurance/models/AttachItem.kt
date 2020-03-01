package com.mahmoud.carsinsurance.models

import android.graphics.Bitmap
import android.net.Uri
import com.google.gson.annotations.SerializedName

class AttachItem {
    @SerializedName("file")
    var file: String? = null

    @SerializedName("type")
    var type: String? = null

    var bitmap: Bitmap? = null
    var uri: Uri? = null

    constructor(type: String?, bitmap: Bitmap?) {
        this.type = type
        this.bitmap = bitmap
    }

    constructor(type: String?, file: String?) {
        this.type = type
        this.file = file
    }

    constructor(type: String?, bitmap: Bitmap?, uri: Uri?) {
        this.type = type
        this.bitmap = bitmap
        this.uri = uri
    }



    constructor()
}