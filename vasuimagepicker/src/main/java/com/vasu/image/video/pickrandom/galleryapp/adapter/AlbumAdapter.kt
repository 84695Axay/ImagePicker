package com.vasu.image.video.pickrandom.galleryapp.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.vasu.image.video.pickrandom.galleryapp.R
import com.vasu.image.video.pickrandom.galleryapp.activity.ImagePickerActivity
import com.vasu.image.video.pickrandom.galleryapp.model.Album

class AlbumAdapter(var albumActivity: ImagePickerActivity, var mAlbumList: List<Album>,var listener : OnAlbumSelected) :
    RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    interface OnAlbumSelected {
        fun onAlbumClicked(position: Int, folderName: String)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mThumbImage = itemView.findViewById<ImageView>(R.id.ivThumbImage)!!
        var mTxtAlbum = itemView.findViewById<TextView>(R.id.txtAlbumName)!!
        var mTxtAlbumCount = itemView.findViewById<TextView>(R.id.txtCount)!!
        var frameLayout = itemView.findViewById<LinearLayout>(R.id.frameLayout)!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(albumActivity).inflate(R.layout.layout_album, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTxtAlbum.text = mAlbumList[position].name
        holder.mTxtAlbumCount.text = mAlbumList[position].mediaCount.toString()

        Glide.with(albumActivity).asBitmap().load(mAlbumList[position].thumbnailUri).addListener(object : RequestListener<Bitmap?> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap?>, isFirstResource: Boolean): Boolean {
                holder.mThumbImage.setImageDrawable(ContextCompat.getDrawable(albumActivity,R.drawable.corrupt_file_black))
                holder.mThumbImage.setPadding(50, 50, 50, 50)
                return true
            }

            override fun onResourceReady(resource: Bitmap?, model: Any, target: Target<Bitmap?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                holder.mThumbImage.setImageBitmap(resource)
                holder.mThumbImage.setPadding(0, 0, 0, 0)
                return true
            }
        }).placeholder(R.drawable.place_holder_photo).override(700).into(holder.mThumbImage)


        holder.itemView.setOnClickListener {
            if (mAlbumList.isNotEmpty()){
                listener.onAlbumClicked(position,mAlbumList[position].name)

            }
        }
    }

    override fun getItemCount(): Int {
        return mAlbumList.size
    }
}