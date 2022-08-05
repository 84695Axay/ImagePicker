package com.vasu.image.video.pickrandom.galleryapp.activity

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vasu.image.video.pickrandom.galleryapp.R
import com.vasu.image.video.pickrandom.galleryapp.adapter.AlbumAdapter
import com.vasu.image.video.pickrandom.galleryapp.adapter.ImageAdapter
import com.vasu.image.video.pickrandom.galleryapp.helper.Constant
import com.vasu.image.video.pickrandom.galleryapp.model.Album
import com.vasu.image.video.pickrandom.galleryapp.model.Config
import com.vasu.image.video.pickrandom.galleryapp.util.GalleryUtil
import gun0912.tedimagepicker.builder.type.MediaType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_image_picker.*
import java.io.File
import java.util.*

class ImagePickerActivity : AppCompatActivity() {

    //Album Activity
    private lateinit var disposable: Disposable
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var mRVAlbum: RecyclerView
    private lateinit var mPBAlbum: ProgressBar
    private lateinit var mAlbumList: List<Album>
    private var config: Config? = null

    //Image Activity
    private lateinit var mImageAdapter: ImageAdapter
    private lateinit var mRVImage: RecyclerView
    private lateinit var mImageList: List<Album>
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

    private var dialog: AlertDialog ?= null

    companion object {
        const val EXTRA_SELECTED_URI = "EXTRA_SELECTED_URI"
        private const val TAG = "ImagePickerActivity"
        private const val SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.jpg"
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker)
        setStatusBarColor()
        initView()
        Constant.lastSelectedPosition = -1
        config = intent.getParcelableExtra(Config.EXTRA_CONFIG)
        if (config!!.isKeepScreenOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        val window = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = config!!.statusBarColor
        }
        mPBAlbum.indeterminateTintList = ColorStateList.valueOf(config!!.progressBarColor)
        toolbar.setBackgroundColor(config!!.toolbarColor)
        toolbarTitle.setTextColor(config!!.toolbarTextColor)
        toolbarTitle.text = config!!.folderTitle
        imgBack.setColorFilter(config!!.toolbarIconColor)
        mainConstraint.setBackgroundColor(config!!.backgroundColor)

        toolbarImage.setBackgroundColor(config!!.toolbarColor)
        imgBackImage.setColorFilter(config!!.toolbarIconColor)
        imgDoneImage.setColorFilter(config!!.toolbarIconColor)
        txtFolderName.setTextColor(config!!.toolbarTextColor)


        try {
            loadMedia(true)
        } catch (e: Exception) { }
        mAlbumList = arrayListOf()

        imgBack.setOnClickListener {
            onBackPressed()
        }

        imgBackImage.setOnClickListener {
            onBackPressed()
        }

        imgDoneImage.setOnClickListener {
            val uri = path
            var data: Intent? = null
            data = Intent(
                this@ImagePickerActivity,
                Class.forName("com.cool.stylish.text.art.fancy.color.creator.activitys.EditAnimationActivity")
            )
            data.putExtra(EXTRA_SELECTED_URI, uri.toString())
            startActivity(data)
        }
    }

    private fun setupAppBar() {
        croptoolbar = findViewById(R.id.croptoolbarAlbum)

        // Set Toolbar Color
        croptoolbar!!.setBackgroundColor(config!!.toolbarColor)
        croptoolbar!!.setTitleTextColor(config!!.toolbarTextColor)
        croptoolbar!!.visibility = View.VISIBLE

        toolbarImage.visibility = View.GONE
        mRVImage.visibility = View.GONE


        val cropToolBarTitle = croptoolbar!!.findViewById<TextView>(R.id.toolbar_title)!!
        cropToolBarTitle.setTextColor(config!!.toolbarTextColor)
        cropToolBarTitle.text = mToolbarTitle

        // Color Toolbar Icons
        val stateButtonDrawable = ContextCompat.getDrawable(baseContext, mToolbarCancelDrawable)
        if (stateButtonDrawable != null) {
            stateButtonDrawable.mutate()
            stateButtonDrawable.setColorFilter(config!!.toolbarIconColor, PorterDuff.Mode.SRC_ATOP)
            croptoolbar!!.navigationIcon = stateButtonDrawable
        }
        setSupportActionBar(croptoolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
    }


    private fun initView() {
        mRVAlbum = findViewById(R.id.rv_album)
        mRVImage = findViewById(R.id.rv_image)
        mPBAlbum = findViewById(R.id.pb_album)
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
                            toolbarImage.visibility = View.VISIBLE
                            txtFolderName.text = folderName

//                            var intent = Intent(this@AlbumActivity, ImageActivity::class.java)
//                            intent.putExtra("position", position)
//                            intent.putExtra("folder", folderName)
//                            intent.putExtra(Config.EXTRA_CONFIG, config)
//                            startActivity(intent)
                        }

                    })
                mPBAlbum.visibility = View.GONE
                mRVAlbum.visibility = View.VISIBLE
                mRVAlbum.layoutManager = layoutManager
                mRVAlbum.adapter = albumAdapter

            }
    }

    override fun onBackPressed() {
        when {
            mRVImage.visibility == View.VISIBLE -> {
                showImage(false)
                imgDoneImage.visibility = View.GONE
                Constant.lastSelectedPosition = -1
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    private fun setImageAdapter(position: Int, folderName: String) {
        toolbarImage.visibility = View.VISIBLE
        txtFolderName.text = folderName

        showImage(true)
        var layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        layoutManager = GridLayoutManager(this, 4)
        mImageAdapter = ImageAdapter(
            this@ImagePickerActivity,
            mAlbumList[position].mediaUris,
            object : ImageAdapter.OnImageClick {
                override fun selectMedia(uri: Uri) {
                    if (uri.toString().isNotEmpty()) {
                        imgDoneImage.visibility = View.VISIBLE
                        path = uri
                    } else {
                        imgDoneImage.visibility = View.INVISIBLE
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
        mRVImage.layoutManager = layoutManager
        mRVImage.adapter = mImageAdapter
    }

    private fun showImage(b: Boolean) {
        if (b) {
            mRVAlbum.visibility = View.GONE
            toolbar.visibility = View.GONE
            mRVImage.visibility = View.VISIBLE
            toolbarImage.visibility = View.VISIBLE
        } else {
            mRVAlbum.visibility = View.VISIBLE
            toolbar.visibility = View.VISIBLE
            mRVImage.visibility = View.GONE
            toolbarImage.visibility = View.GONE
        }
    }

    private fun removeFragmentFromScreen() {
        try {
            croptoolbar!!.visibility = View.GONE
            toolbarImage.visibility = View.VISIBLE
            mRVImage.visibility = View.VISIBLE
        } catch (e: Exception) { }
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
        if (dialog!=null && dialog!!.isShowing){
            dialog!!.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
//        Toast.makeText(this@ImagePickerActivity, "resume", Toast.LENGTH_SHORT).show()
        if (dialog!=null && dialog!!.isShowing){
            dialog!!.dismiss()
        }
    }

}