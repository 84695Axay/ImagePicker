package com.vasu.image.video.pickrandom.galleryapp.model

import android.net.Uri

data class Media(
    val albumName: String,
    val uri: Uri,
    val dateAddedSecond: Long,
    var isCorrupted : Boolean
)