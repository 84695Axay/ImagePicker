package com.example.myadslibrary

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.facebook.ads.Ad
import com.facebook.ads.AdSettings
import com.facebook.ads.InterstitialAdListener
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.adunit.adapter.utility.AdapterErrorType
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.InterstitialListener

class InterstitialAdsHelper {

    companion object {
        val TAG = "InterstitialAdsHelper"
        var valueIn = 0

        var isLoadingAds = false
        var interstitialAd1: InterstitialAd? = null
        var isInterstitialAdShowing: Boolean = false
        var interstitialAdListener: InterstitialAdListener? = null

        var rewardedInterstitialAd: RewardedInterstitialAd? = null
        var interstitialAdFB: com.facebook.ads.InterstitialAd? = null
        var instence: InterstitialAdsHelper? = null
            get() {

                if (field == null) {
                    field = InterstitialAdsHelper()
                }
                return field
            }
    }


     fun loadRewardedInterstitialAd(context: Activity,var string: String) {
        if (rewardedInterstitialAd == null && !isLoadingAds) {
            val adRequest = AdRequest.Builder().build()

            // Load an ad.
            RewardedInterstitialAd.load(
                context,
                "ca-app-pub-3940256099942544/5354046379",
                adRequest,
                object : RewardedInterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        super.onAdFailedToLoad(adError)
                        Log.d(TAG, "onAdFailedToLoad: ${adError.message}")
                        isLoadingAds = false
                        rewardedInterstitialAd = null
                    }

                    override fun onAdLoaded(rewardedAd: RewardedInterstitialAd) {
                        super.onAdLoaded(rewardedAd)
                        Log.d(TAG, "Ad was loaded.")

                        rewardedInterstitialAd = rewardedAd
                        isLoadingAds = true
                    }
                }
            )
        }
    }
     fun loadRewardedInterstitialAdSh(context: Activity,
                                      onAdDismissed: (() -> Unit)? = null,
                                      onAdShowed: (() -> Unit)? = null,
                                      onUserEarned: (() -> Unit)? = null,
                                      onAdFailedToShow: (() -> Unit)? = null) {
        if (rewardedInterstitialAd != null && isLoadingAds) {
            rewardedInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d(TAG, "Ad was clicked.")
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d(TAG, "Ad dismissed fullscreen content.")
                    rewardedInterstitialAd = null
                    onAdDismissed?.invoke()
                    loadRewardedInterstitialAd(context)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when ad fails to show.
                    Log.e(TAG, "Ad failed to show fullscreen content.")
                    rewardedInterstitialAd = null
                    onAdFailedToShow?.invoke()

                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "Ad recorded an impression.")
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "Ad showed fullscreen content.")
                    onAdShowed?.invoke()

                }
            }
            rewardedInterstitialAd?.show(/* Activity */ context
            ) {
                onUserEarned?.invoke()
            }
        }
    }


    private fun isNetworkAvailable(context: Activity): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    fun loadInterstitialAd(context: Activity) {
        if (interstitialAd1 == null) {
            loadInterstitialAd1(context)
        }
    }

    private fun loadInterstitialAd1(context: Activity) {
        if (!isNetworkAvailable(context)) {
            return
        }

        val id = if (valueIn in 3..8) {
            "dsdsdsd"
        } else "ca-app-pub-394025609sd9942544/1033173712"
        Log.d(TAG, "id: id ${id}")

        InterstitialAd.load(
            context,
            id,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd1 = null
                    Log.d(TAG, "onAdFailedToLoad: interstitialAd ${adError.message}")
                    loadInterstitialAdFB(context)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {

                    Log.d(TAG, "onAdLoaded: interstitialAd1")

                    interstitialAd1 = interstitialAd
                }
            })

    }

    private fun loadInterstitialAdFB(
        context: Activity,
        onDismissed: (() -> Unit)? = null,
        onShow: (() -> Unit)? = null,
        onError: (() -> Unit)? = null
    ) {
        if (!isNetworkAvailable(context)) {
            return
        }
        var string = "939883980106235_1215036629257634"
        if (valueIn in 3..5) {
            AdSettings.addTestDevice("1515ac53-ss7c29-49eb-9a95-3b7dffff2be7");
            string = "939883980106235_1215036629s257634"
        } else {
            AdSettings.addTestDevice("1515ac53-7c29-49eb-9a95-3b7dffff2be7");
            string = "939883980106235_1215036629257634"
        }

        interstitialAdFB =
            com.facebook.ads.InterstitialAd(context, string)
        interstitialAdListener =
            object : InterstitialAdListener {
                override fun onInterstitialDisplayed(ad: Ad) {
                    // Interstitial ad displayed callback
                    Log.e(TAG, "FBInterstitial ad displayed.")
                    onShow?.invoke()
                }

                override fun onInterstitialDismissed(ad: Ad) {
                    // Interstitial dismissed callback
                    Log.e(TAG, "FBInterstitial ad dismissed.")
                    interstitialAdFB = null
                    interstitialAd1 = null
                    valueIn++

                    Log.e(TAG, " VVVV FB $valueIn")

                    onDismissed?.let { it() }
                    loadInterstitialAd(context)

                }

                override fun onError(p0: Ad?, p1: com.facebook.ads.AdError?) {
                    Log.e(TAG, "FBInterstitial ad failed to load: $valueIn " + p1!!.errorMessage)
                    onError?.let { it() }
                    initIronSource(context)

                }

                override fun onAdLoaded(ad: Ad) {
                    // Interstitial ad is loaded and ready to be displayed
                    Log.d(TAG, "FBInterstitial ad is loaded and ready to be displayed!")
                    // Show the ad
                }

                override fun onAdClicked(ad: Ad) {
                    // Ad clicked callback
                    Log.d(TAG, "FBInterstitial ad clicked!")
                }

                override fun onLoggingImpression(ad: Ad) {
                    // Ad impression logged callback
                    Log.d(TAG, "FBInterstitial ad impression logged!")
                }
            }

        interstitialAdFB!!.loadAd(
            interstitialAdFB!!.buildLoadAdConfig()
                .withAdListener(interstitialAdListener)
                .build()
        )
    }

    private fun initIronSource(context: Activity) {
//        SupersonicConfig.getConfigObj().clientSideCallbacks = true


        IronSource.init(
            context,
            "15af0c9ed",
            IronSource.AD_UNIT.INTERSTITIAL,
            IronSource.AD_UNIT.REWARDED_VIDEO
        )
        IronSource.loadInterstitial()
    }

    fun showInterstitialAd(
        context: Activity,
        onAdShowed: () -> Unit,
        onAdDismissed: () -> Unit,
        onAdFailedToShow: () -> Unit,
        onPro: () -> Unit,
        isPro: Boolean = false
    ) {
        if (isPro) {
            onPro()
        } else {
            try {
                Log.d(TAG, "showInterstitialAd: $interstitialAd1")
                if (interstitialAd1 != null) {
                    interstitialAd1!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {

                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                Handler(Looper.getMainLooper()).post {
                                    onAdDismissed()
                                }
                                valueIn++
                                Log.e(TAG, " VVVV G $valueIn")

                                interstitialAd1 = null
                                loadInterstitialAd(context)
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                super.onAdFailedToShowFullScreenContent(p0)
                                onAdFailedToShow()
                                Log.d("ViewPhotoActivity.TAG", "onClickId: onError ${p0.message}")

                                isInterstitialAdShowing = false
                                interstitialAd1 = null

                                loadInterstitialAd(context)

                            }

                            override fun onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent()
                                isInterstitialAdShowing = true
                                onAdShowed()
                            }

                        }
                    interstitialAd1!!.show(context as Activity)
                } else {
                    onAdFailedToShow()
                    Log.d("ViewPhotoActivity.TAG", "onClickId: onError null")
                    isInterstitialAdShowing = false
                    loadInterstitialAd(context)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                onAdFailedToShow()
            }
        }
    }

    fun showInterstitialAdFB(
        context: Activity,
        onDismissed: () -> Unit,
        onShow: () -> Unit,
        onError: () -> Unit,
        onPro: () -> Unit,
        isPro: Boolean = false
    ) {
        if (isPro) {
            onPro()
        } else {
            try {
                if (interstitialAdFB == null || !interstitialAdFB!!.isAdLoaded) {
                    return
                }
                interstitialAdListener =
                    object : InterstitialAdListener {
                        override fun onInterstitialDisplayed(ad: Ad) {
                            // Interstitial ad displayed callback
                            Log.e(TAG, "FBInterstitial ad displayed.")
                            onShow?.invoke()
                        }

                        override fun onInterstitialDismissed(ad: Ad) {
                            // Interstitial dismissed callback
                            Log.e(TAG, "FBInterstitial ad dismissed.")
                            interstitialAdFB = null
                            interstitialAd1 = null
                            valueIn++

                            Log.e(TAG, " VVVV FB $valueIn")

                            onDismissed?.let { it() }
                            loadInterstitialAd(context)

                        }

                        override fun onError(p0: Ad?, p1: com.facebook.ads.AdError?) {
                            Log.e(
                                TAG,
                                "FBInterstitial ad failed to load: $valueIn " + p1!!.errorMessage
                            )
                            onError?.let { it() }
                            initIronSource(context)

                        }

                        override fun onAdLoaded(ad: Ad) {
                            // Interstitial ad is loaded and ready to be displayed
                            Log.d(TAG, "FBInterstitial ad is loaded and ready to be displayed!")
                            // Show the ad
                        }

                        override fun onAdClicked(ad: Ad) {
                            // Ad clicked callback
                            Log.d(TAG, "FBInterstitial ad clicked!")
                        }

                        override fun onLoggingImpression(ad: Ad) {
                            // Ad impression logged callback
                            Log.d(TAG, "FBInterstitial ad impression logged!")
                        }
                    }
                interstitialAdFB!!.buildLoadAdConfig()
                    .withAdListener(interstitialAdListener)
                interstitialAdFB!!.show()
            } catch (e: Exception) {
                e.printStackTrace()
                onError()
            }
        }
    }

    fun showInterstitialAdIron(
        context: Activity,
        onDismissed: () -> Unit,
        onShow: () -> Unit,
        onError: () -> Unit,
        onPro: () -> Unit,
        isPro: Boolean = false
    ) {
        if (IronSource.isInterstitialReady()) {
            //show the interstitial
            IronSource.setInterstitialListener(object :
                com.ironsource.mediationsdk.adunit.adapter.listener.InterstitialAdListener,
                InterstitialListener {
                override fun onInterstitialAdReady() {
                    Log.d(TAG, "ironSource => IronSource onInterstitialAdReady: ")
                }

                override fun onInterstitialAdLoadFailed(p0: IronSourceError?) {
                    Log.d(TAG, "ironSource => IronSource onInterstitialAdLoadFailed: ")
                }

                override fun onInterstitialAdOpened() {
                    onShow()

                    Log.d(TAG, "ironSource => IronSource onInterstitialAdOpened: ")
                }

                override fun onInterstitialAdClosed() {
                    Log.d(TAG, "ironSource => IronSource onInterstitialAdClosed: $valueIn")
                    valueIn++
                    if (valueIn >= 8) {
                        valueIn = 0
                    }
                    onDismissed()
                    loadInterstitialAd(context)
                }

                override fun onInterstitialAdShowSucceeded() {
                    Log.d(TAG, "ironSource => IronSource onInterstitialAdShowSucceeded: ")
                }

                override fun onInterstitialAdShowFailed(p0: IronSourceError?) {
                    Log.d(TAG, "ironSource => IronSource onInterstitialAdShowFailed: ")
                }

                override fun onInterstitialAdClicked() {
                    Log.d(TAG, "ironSource => IronSource onInterstitialAdClicked: ")
                }

                override fun onAdLoadSuccess() {
                    Log.d(TAG, "ironSource => IronSource onAdLoadSuccess: ")

                }

                override fun onAdLoadFailed(p0: AdapterErrorType, p1: Int, p2: String?) {
                    Log.d(TAG, "ironSource => IronSource onAdLoadFailed: ")
                    Log.d(TAG, "ironSource => IronSource Error p0 $p0")
                    Log.d(TAG, "ironSource => IronSource Error p1 $p1")
                    Log.d(TAG, "ironSource => IronSource Error p2 $p2")
                }

                override fun onAdClicked() {
                    Log.d(TAG, "ironSource => IronSource onAdClicked: ")
                }

                override fun onAdOpened() {
                    Log.d(TAG, "ironSource => IronSource onAdOpened: ")
                }

                override fun onAdClosed() {
                    Log.d(TAG, "ironSource => IronSource onAdClosed: ")
                }

                override fun onAdShowFailed(p0: Int, p1: String?) {
                    Log.d(TAG, "ironSource => IronSource onAdShowFailed: ")
                }

                override fun onAdShowSuccess() {
                    Log.d(TAG, "ironSource => IronSource onAdShowSuccess: ")
                }

                override fun onAdVisible() {
                    Log.d(TAG, "ironSource => IronSource onAdVisible: ")
                }

                override fun onAdStarted() {
                    Log.d(TAG, "ironSource => IronSource onAdStarted: ")
                }

                override fun onAdEnded() {
                    Log.d(TAG, "ironSource => IronSource onAdEnded: ")
                }

            })

            IronSource.showInterstitial()
        } else {
            onError()
        }
    }

}