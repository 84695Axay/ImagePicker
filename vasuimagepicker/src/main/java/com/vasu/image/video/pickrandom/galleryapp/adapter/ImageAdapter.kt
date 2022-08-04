package com.vasu.image.video.pickrandom.galleryapp.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.text.Html
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.vasu.image.video.pickrandom.galleryapp.R
import com.vasu.image.video.pickrandom.galleryapp.activity.ImagePickerActivity
import com.vasu.image.video.pickrandom.galleryapp.helper.Constant
import com.vasu.image.video.pickrandom.galleryapp.model.Media

class ImageAdapter(
    var mContext: ImagePickerActivity,
    var mediaUris: List<Media>,
    var listener: OnImageClick
) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private var lastPosition = -1
//    var mArrayList = mediaUris as ArrayList

    interface OnImageClick {
        fun selectMedia(uri: Uri)
        fun sendDialog(dialog: AlertDialog)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mImage = itemView.findViewById<ImageView>(R.id.ivImage)!!
        var mSelectImage = itemView.findViewById<ImageView>(R.id.ivSelectImage)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.layout_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.setIsRecyclable(false)

        if (Constant.lastSelectedPosition == position) {
            holder.mSelectImage.visibility = View.VISIBLE
        } else {
            holder.mSelectImage.visibility = View.INVISIBLE
        }
        Glide.with(mContext).asBitmap().load(mediaUris[position].uri).addListener(object :
            RequestListener<Bitmap?> {
            override fun onLoadFailed(
                e: GlideException?,
                model1: Any,
                target: Target<Bitmap?>,
                isFirstResource: Boolean
            ): Boolean {
                mediaUris[position].isCorrupted = true
                holder.mImage.setBackgroundColor(Color.WHITE)
                holder.mImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.corrupt_file_black
                    )
                )
                holder.mImage.setPadding(50, 50, 50, 50)
                return true
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model1: Any,
                target: Target<Bitmap?>,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                mediaUris[position].isCorrupted = false
                holder.mImage.setBackgroundColor(0)
                holder.mImage.setPadding(0, 0, 0, 0)
                holder.mImage.setImageBitmap(resource)
                return true
            }
        }).placeholder(R.drawable.place_holder_photo).into(holder.mImage)

        holder.itemView.setOnClickListener {
            if (mediaUris[position].isCorrupted) {
//                var dialog = AlertDialog.Builder(mContext,R.style.alertDialogTheme).create()
                var dialog = AlertDialog.Builder(mContext).create()
                dialog.setMessage("This image is corrupted. Please select another image.")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dialog.setButton(Dialog.BUTTON_POSITIVE, HtmlCompat.fromHtml("<span style=\"color:black\">Ok</span>",HtmlCompat.FROM_HTML_MODE_LEGACY)) { _, _ ->
                        dialog.dismiss()
                    }
                } else {
                    dialog.setButton(Dialog.BUTTON_POSITIVE, HtmlCompat.fromHtml("<span style=\"color:black\">Ok</span>",HtmlCompat.FROM_HTML_MODE_LEGACY)) { _, _ ->
                        dialog.dismiss()
                    }
                }

                dialog.show()
                listener.sendDialog(dialog)
            } else {
                Constant.lastSelectedPosition = position
                listener.selectMedia(mediaUris[position].uri)
                notifyDataSetChanged()
            }

        }

    }

    fun removeSelection() {
        Constant.lastSelectedPosition = -1
    }

    override fun getItemCount(): Int {
        return mediaUris.size
    }
}