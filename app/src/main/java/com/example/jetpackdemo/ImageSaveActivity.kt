package com.example.jetpackdemo

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import com.example.jetpackdemo.databinding.ImageSaveBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class ImageSaveActivity : AppCompatActivity() {

    var imageSaveBin: ImageSaveBinding? = null
    var customWidth: Int? = 3883
    var customHeight: Int? = 5825

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageSaveBin = ImageSaveBinding.inflate(LayoutInflater.from(this))
        setContentView(imageSaveBin!!.root)
        InterstitialAdsHelper.instence!!.loadInterstitialAd(this)
        InterstitialAdsHelper.instence!!.loadRewardedInterstitialAd(this)
    }

    override fun onBackPressed() {

        if (InterstitialAdsHelper.rewardedInterstitialAd != null) {
            InterstitialAdsHelper.instence!!.loadRewardedInterstitialAdSh(
                this,
                onAdDismissed = {
                    Toast.makeText(this, "RewardedInterstitial onAdDismissed", Toast.LENGTH_SHORT).show()
                },
                onAdFailedToShow = {
                    Toast.makeText(this, "RewardedInterstitial onAdFailedToShow", Toast.LENGTH_SHORT).show()
                },
                onAdShowed = {
                    Toast.makeText(this, "RewardedInterstitial onAdShowed", Toast.LENGTH_SHORT).show()
                })
        }


/*
        if (InterstitialAdsHelper.interstitialAd1 != null) {
            InterstitialAdsHelper.instence!!.showInterstitialAd(
                this,
                onAdDismissed = {
                    Toast.makeText(this, "Google onAdDismissed", Toast.LENGTH_SHORT).show()
                },
                onAdFailedToShow = {
                    Toast.makeText(this, "Google onAdFailedToShow", Toast.LENGTH_SHORT).show()

                },
                onPro = {
                    Toast.makeText(this, "Google onPro", Toast.LENGTH_SHORT).show()
                },
                onAdShowed = {
                    Toast.makeText(this, "Google onAdShowed", Toast.LENGTH_SHORT).show()
                })
        } else if (InterstitialAdsHelper.interstitialAdFB != null && InterstitialAdsHelper.interstitialAdFB!!.isAdLoaded) {
            InterstitialAdsHelper.instence!!.showInterstitialAdFB(
                this,
                onDismissed = {
                    Toast.makeText(this, "FB onDismissed", Toast.LENGTH_SHORT).show()
                },
                onError = {
                    Toast.makeText(this, "FB onError", Toast.LENGTH_SHORT).show()
                },
                onPro = {
                    Toast.makeText(this, "FB onPro", Toast.LENGTH_SHORT).show()
                },
                onShow = {
                    Toast.makeText(this, "FB onShow", Toast.LENGTH_SHORT).show()
                })
        } else {
            InterstitialAdsHelper.instence!!.showInterstitialAdIron(this,
                onDismissed = {
                    Toast.makeText(this, "Iron onDismissed", Toast.LENGTH_SHORT).show()
                },
                onError = {
                    Toast.makeText(this, "Iron onError", Toast.LENGTH_SHORT).show()
                },
                onPro = {
                    Toast.makeText(this, "Iron onPro", Toast.LENGTH_SHORT).show()
                },
                onShow = {
                    Toast.makeText(this, "Iron onShow", Toast.LENGTH_SHORT).show()
                })
        }
*/
    }
}