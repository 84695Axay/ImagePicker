package com.example.mygallerypicker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.jetpackdemo.databinding.ImageSaveBinding
import com.vasu.image.video.pickrandom.galleryapp.VasuImagePicker

class ImageSaveActivity : AppCompatActivity() {
    var imageSaveBin: ImageSaveBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageSaveBin = ImageSaveBinding.inflate(LayoutInflater.from(this))
        setContentView(imageSaveBin!!.root)

        imageSaveBin!!.imgGallery.setOnClickListener {
            VasuImagePicker.ActivityBuilder(this)
                .setFolderMode(true)
                .setFolderTitle("Gallery")
                .setMultipleMode(false)
                .setImageCount(1)
                .setMaxSize(10)
                .setBackgroundColor("#FFFFFF")
                .setToolbarColor("#FFFFFF")
                .setToolbarTextColor("#000000")
                .setToolbarIconColor("#000000")
                .setStatusBarColor("#FFFFFF")
                .setProgressBarColor("#50b1ed")
                .setAlwaysShowDoneButton(true)
                .setRequestCode(1010)
                .setKeepScreenOn(true)
                .start()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1010 && resultCode == -1) {
            Glide.with(this).load(data!!.getStringExtra("EXTRA_SELECTED_URI"))
                .into(imageSaveBin!!.imgGallery)
        }
    }
}