package com.vasu.image.video.pickrandom.galleryapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.vasu.image.video.pickrandom.galleryapp.activity.ImagePickerActivity
import com.vasu.image.video.pickrandom.galleryapp.model.Config
import com.vasu.image.video.pickrandom.galleryapp.model.SavePath

open class VasuImagePicker {
    private var config: Config? = null

    public class ActivityBuilder(private val activity: Activity) :
        VasuImagePicker.Builder(
            activity
        ) {
        override fun start() {
            val intent = intent
            val requestCode =
                if (config.requestCode != 0) config.requestCode else Config.RC_PICK_IMAGES
            if (true) {
                activity.startActivityForResult(intent, requestCode)
            } else {
                activity.overridePendingTransition(0, 0)
                activity.startActivityForResult(intent, requestCode)
            }
        }

        override val intent: Intent
            get() {
                val intent: Intent = Intent(activity, ImagePickerActivity::class.java)
                intent.putExtra(Config.EXTRA_CONFIG, config)
                return intent
            }

    }

    abstract class Builder : BaseBuilder {
        constructor(activity: Activity?) : super(activity) {}
        constructor(fragment: Fragment) : super(fragment.context) {}

        fun setToolbarColor(toolbarColor: String?): Builder {
            config.setToolbarColor(toolbarColor)
            return this
        }

        fun setStatusBarColor(statusBarColor: String?): Builder {
            config.setStatusBarColor(statusBarColor)
            return this
        }

        fun setToolbarTextColor(toolbarTextColor: String?): Builder {
            config.setToolbarTextColor(toolbarTextColor)
            return this
        }

        fun setToolbarIconColor(toolbarIconColor: String?): Builder {
            config.setToolbarIconColor(toolbarIconColor)
            return this
        }

        fun setProgressBarColor(progressBarColor: String?): Builder {
            config.setProgressBarColor(progressBarColor)
            return this
        }

        fun setBackgroundColor(backgroundColor: String?): Builder {
            config.setBackgroundColor(backgroundColor)
            return this
        }

        fun setMultipleMode(isMultipleMode: Boolean): Builder {
            config.isMultipleMode = true
            return this
        }

        fun setImageCount(count: Int): Builder {
            config.imageCount = count
            return this
        }

        fun setFolderMode(isFolderMode: Boolean): Builder {
            config.isFolderMode = true
            return this
        }

        fun setMaxSize(maxSize: Int): Builder {
            config.maxSize = maxSize
            return this
        }

        fun setDoneTitle(doneTitle: String?): Builder {
            config.doneTitle = doneTitle
            return this
        }

        fun setFolderTitle(folderTitle: String?): Builder {
            config.folderTitle = folderTitle
            return this
        }

        fun setImageTitle(imageTitle: String?): Builder {
            config.imageTitle = imageTitle
            return this
        }

        fun setLimitMessage(message: String?): Builder {
            config.limitMessage = message
            return this
        }

        fun setSavePath(path: String?): Builder {
            config.savePath = SavePath(path, false)
            return this
        }

        fun setAlwaysShowDoneButton(isAlwaysShowDoneButton: Boolean): Builder {
            config.isAlwaysShowDoneButton = isAlwaysShowDoneButton
            return this
        }

        fun setKeepScreenOn(keepScreenOn: Boolean): Builder {
            config.isKeepScreenOn = keepScreenOn
            return this
        }
        fun setRequestCode(requestCode: Int): Builder {
            config.requestCode = requestCode
            return this
        }

        abstract fun start()
        abstract val intent: Intent?
    }

    abstract class BaseBuilder(context: Context?) {
        var config: Config = Config()

        init {
            val resources = context!!.resources
            config.isMultipleMode = true
            config.isFolderMode = true
            config.maxSize = Config.MAX_SIZE
            config.doneTitle = resources.getString(R.string.imagepicker_action_done)
            config.folderTitle = resources.getString(R.string.imagepicker_title_folder)
            config.imageTitle = resources.getString(R.string.imagepicker_title_image)
            config.limitMessage = resources.getString(R.string.imagepicker_msg_limit_images)
            config.savePath = SavePath.DEFAULT
            config.isAlwaysShowDoneButton = false
            config.isKeepScreenOn = false
        }
    }
}