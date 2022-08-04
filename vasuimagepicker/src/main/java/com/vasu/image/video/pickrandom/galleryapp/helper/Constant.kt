package com.vasu.image.video.pickrandom.galleryapp.helper

import android.graphics.Bitmap
import android.net.Uri
import java.util.*

object Constant {
    var lastSelectedURI: Uri? = null
    var lastSelectedPosition: Int = -1
    var selectedImage: List<Uri> = ArrayList<Uri>()
    const val WRITE_EXTERNAL_STORAGE_CODE = 124

    var finalBitmap: Bitmap? = null

}