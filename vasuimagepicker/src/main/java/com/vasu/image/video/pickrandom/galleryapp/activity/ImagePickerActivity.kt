package com.vasu.image.video.pickrandom.galleryapp.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vasu.image.video.pickrandom.galleryapp.adapter.AlbumAdapter
import com.vasu.image.video.pickrandom.galleryapp.adapter.ImageAdapter
import com.vasu.image.video.pickrandom.galleryapp.databinding.ActivityImagePickerBinding
import com.vasu.image.video.pickrandom.galleryapp.helper.Constant
import com.vasu.image.video.pickrandom.galleryapp.model.Album
import com.vasu.image.video.pickrandom.galleryapp.model.Config
import com.vasu.image.video.pickrandom.galleryapp.util.GalleryUtil
import gun0912.tedimagepicker.builder.type.MediaType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ImagePickerActivity : AppCompatActivity() {

    //Album Activity
    private lateinit var disposable: Disposable
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var mAlbumList: List<Album>
    private var config: Config? = null

    //Image Activity
    private lateinit var mImageAdapter: ImageAdapter
    private lateinit var path: Uri

    //Ucrop Fragment
    private var mToolbarColor = 0
    private var mStatusBarColor = 0
    private var mToolbarWidgetColor = 0
    private var mToolbarTitle: String? = null

    @DrawableRes
    private var mToolbarCancelDrawable = 0

    @DrawableRes
    private var mToolbarCropDrawable = 0
    private var croptoolbar: Toolbar? = null
    private var mShowLoader = false

    private var dialog: AlertDialog? = null
    var activityImagePickerBinding: ActivityImagePickerBinding? = null

    companion object {
        const val EXTRA_SELECTED_URI = "EXTRA_SELECTED_URI"
        private const val TAG = "ImagePickerActivity"
        private const val SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.jpg"
    }

    fun checkForPermission(): Boolean = run {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            1001
        )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityImagePickerBinding = ActivityImagePickerBinding.inflate(LayoutInflater.from(this))
        setContentView(activityImagePickerBinding!!.root)
        if (checkForPermission()) {
            activityImagePickerBinding!!.imgNoFound.visibility = View.GONE
            activityImagePickerBinding!!.pbAlbum.visibility = View.GONE
            initView()
        } else {
            activityImagePickerBinding!!.pbAlbum.visibility = View.GONE
            activityImagePickerBinding!!.imgNoFound.visibility = View.VISIBLE
            activityImagePickerBinding!!.imgNoFound.setOnClickListener {
                requestPermissions()
            }
        }

    }

    fun startInstalledAppDetailsActivity(context: Activity?) {
        if (context == null) {
            return
        }
        val i = Intent()
        i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.data = Uri.parse("package:" + context.packageName)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        context.startActivity(i)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1001 -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            initView()
                        }, 200)
                    } else {
                        val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        if (!showRationale) {
                            val builder = android.app.AlertDialog.Builder(this)
                            builder.setTitle("Permission Required")
                            builder.setMessage("Storage Permission are required to save Image into External Storage")
                            builder.setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                startInstalledAppDetailsActivity(this@ImagePickerActivity)
                            }
                            builder.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                            builder.create().show()
                        }
                    }
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarColor()
        }
        activityImagePickerBinding!!.pbAlbum.visibility = View.VISIBLE
        activityImagePickerBinding!!.imgNoFound.visibility = View.GONE
        Constant.lastSelectedPosition = -1
        config = intent.getParcelableExtra(Config.EXTRA_CONFIG)
        if (config!!.isKeepScreenOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        val window = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = config!!.statusBarColor
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activityImagePickerBinding!!.pbAlbum.indeterminateTintList =
                ColorStateList.valueOf(config!!.progressBarColor)
        }
        activityImagePickerBinding!!.toolbar.setBackgroundColor(config!!.toolbarColor)
        activityImagePickerBinding!!.toolbarTitle.setTextColor(config!!.toolbarTextColor)
        activityImagePickerBinding!!.toolbarTitle.text = config!!.folderTitle
        activityImagePickerBinding!!.imgBack.setColorFilter(config!!.toolbarIconColor)
        activityImagePickerBinding!!.mainConstraint.setBackgroundColor(config!!.backgroundColor)

        activityImagePickerBinding!!.toolbarImage.setBackgroundColor(config!!.toolbarColor)
        activityImagePickerBinding!!.imgBackImage.setColorFilter(config!!.toolbarIconColor)
        activityImagePickerBinding!!.imgDoneImage.setColorFilter(config!!.toolbarIconColor)
        activityImagePickerBinding!!.txtFolderName.setTextColor(config!!.toolbarTextColor)


        try {
            loadMedia(true)
        } catch (e: Exception) {
        }
        mAlbumList = arrayListOf()

        activityImagePickerBinding!!.imgBack.setOnClickListener {
            onBackPressed()
        }

        activityImagePickerBinding!!.imgBackImage.setOnClickListener {
            onBackPressed()
        }

        activityImagePickerBinding!!.imgDoneImage.setOnClickListener {
            val uri = path
            val data: Intent = Intent()
            data.putExtra(EXTRA_SELECTED_URI, uri.toString())
            setResult(-1, data)
            finish()
        }
    }

    private fun loadMedia(isRefresh: Boolean = false) {
        disposable = GalleryUtil.getMedia(this, MediaType.IMAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { albumList: List<Album> ->
                mAlbumList = albumList

                for (i in albumList) {

                    Log.d("TAG", "loadMedia: ${i.name}")
                }
//                Toast.makeText(this@AlbumActivity,mAlbumList[0].mediaUris.size.toString(), Toast.LENGTH_SHORT).show()
                var layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                layoutManager = GridLayoutManager(this, 2)
                albumAdapter = AlbumAdapter(
                    this@ImagePickerActivity,
                    mAlbumList,
                    object : AlbumAdapter.OnAlbumSelected {
                        override fun onAlbumClicked(
                            position: Int,
                            folderName: String
                        ) {
                            setImageAdapter(position, folderName)
                            activityImagePickerBinding!!.toolbarImage.visibility = View.VISIBLE
                            activityImagePickerBinding!!.txtFolderName.text = folderName

//                            var intent = Intent(this@AlbumActivity, ImageActivity::class.java)
//                            intent.putExtra("position", position)
//                            intent.putExtra("folder", folderName)
//                            intent.putExtra(Config.EXTRA_CONFIG, config)
//                            startActivity(intent)
                        }

                    })
                activityImagePickerBinding!!.pbAlbum.visibility = View.GONE
                activityImagePickerBinding!!.rvAlbum.visibility = View.VISIBLE
                activityImagePickerBinding!!.rvAlbum.layoutManager = layoutManager
                activityImagePickerBinding!!.rvAlbum.adapter = albumAdapter

            }
    }

    override fun onBackPressed() {
        when (activityImagePickerBinding!!.rvImage.visibility) {
            View.VISIBLE -> {
                showImage(false)
                activityImagePickerBinding!!.imgDoneImage.visibility = View.INVISIBLE
                Constant.lastSelectedPosition = -1
            }
            else -> {
               finish()
            }
        }
    }

    private fun setImageAdapter(position: Int, folderName: String) {
        activityImagePickerBinding!!.toolbarImage.visibility = View.VISIBLE
        activityImagePickerBinding!!.txtFolderName.text = folderName

        showImage(true)
        var layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        layoutManager = GridLayoutManager(this, 4)
        mImageAdapter = ImageAdapter(
            this@ImagePickerActivity,
            mAlbumList[position].mediaUris,
            object : ImageAdapter.OnImageClick {
                override fun selectMedia(uri: Uri) {
                    if (uri.toString().isNotEmpty()) {
                        activityImagePickerBinding!!.imgDoneImage.visibility = View.VISIBLE
                        path = uri
                    } else {
                        activityImagePickerBinding!!.imgDoneImage.visibility = View.INVISIBLE
                        Toast.makeText(
                            this@ImagePickerActivity,
                            "Please Select Image",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun sendDialog(d: AlertDialog) {
                    dialog = d
                }

            })
        activityImagePickerBinding!!.rvImage.layoutManager = layoutManager
        activityImagePickerBinding!!.rvImage.adapter = mImageAdapter
    }

    private fun showImage(b: Boolean) {
        if (b) {
            activityImagePickerBinding!!.rvAlbum.visibility = View.GONE
            activityImagePickerBinding!!.toolbar.visibility = View.GONE
            activityImagePickerBinding!!.rvImage.visibility = View.VISIBLE
            activityImagePickerBinding!!.toolbarImage.visibility = View.VISIBLE
        } else {
            activityImagePickerBinding!!.rvAlbum.visibility = View.VISIBLE
            activityImagePickerBinding!!.toolbar.visibility = View.VISIBLE
            activityImagePickerBinding!!.rvImage.visibility = View.GONE
            activityImagePickerBinding!!.toolbarImage.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarColor() {
        val window: Window = window
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = Color.parseColor("#6992C6")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)


//                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                window.statusBarColor = Color.parseColor("#FFFFFF")
            }
        }
    }

    override fun onPause() {
        super.onPause()
//        Toast.makeText(this@ImagePickerActivity, "pause", Toast.LENGTH_SHORT).show()
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
//        Toast.makeText(this@ImagePickerActivity, "resume", Toast.LENGTH_SHORT).show()
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

}