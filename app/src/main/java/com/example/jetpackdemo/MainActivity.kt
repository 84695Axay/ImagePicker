package com.example.jetpackdemo

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class MainActivity : AppCompatActivity() {
    val mStoreViewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // update UI
        // update UI

        mStoreViewModel.getFruitList().observe(this, { fruitlist ->
            // update UI
            val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
                this,
                android.R.layout.simple_list_item_1, android.R.id.text1, fruitlist
            )
            findViewById<ListView>(R.id.listView).adapter = adapter

        })
        findViewById<ListView>(R.id.listView).setOnItemClickListener { parent, view, position, id ->
            mStoreViewModel.sortVal(position)
        }
    }
}