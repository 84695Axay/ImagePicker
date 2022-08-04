package com.vasu.image.video.pickrandom.galleryapp.model

import android.net.Uri

data class Album(
    val name: String,
    val thumbnailUri: Uri,
    val mediaUris: List<Media>
) {
    val mediaCount: Int = mediaUris.size
}