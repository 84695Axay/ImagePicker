package com.vasu.image.video.pickrandom.galleryapp.util

import android.content.Context
import android.widget.Toast

object ToastUtil {
    lateinit var context: Context
    var toastAction: ((String) -> Unit)? = null

    fun showToast(text: String) {
        toastAction?.invoke(text) ?: Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}
