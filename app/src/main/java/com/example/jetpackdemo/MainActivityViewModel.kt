package com.example.jetpackdemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import java.util.*

class MainActivityViewModel : ViewModel() {
    private val TAG = MainActivityViewModel::class.java.simpleName
    var fruitList: MutableLiveData<List<String?>>? = null
    val fruitsStringList: MutableList<String?> = ArrayList()

    fun getFruitList(): LiveData<List<String?>> {
        if (fruitList == null) {
            fruitList = MutableLiveData()
            loadFruits()
        }
        return fruitList as MutableLiveData<List<String?>>
    }

    private fun loadFruits() {
        viewModelScope.launch {
            delay(5000)
            withContext(Dispatchers.IO) {
                fruitsStringList.add("Mango")
                fruitsStringList.add("Apple")
                fruitsStringList.add("Orange")
                fruitsStringList.add("Banana")
                fruitsStringList.add("Grapes")
                fruitsStringList.add("Grapes")
                fruitsStringList.add("Grapes")
                fruitsStringList.add("Grapes")
                fruitsStringList.add("Grapes")
                fruitsStringList.add("Grapes")
                fruitsStringList.add("Grapes")
                fruitsStringList.add("Grapes")
                fruitsStringList.add("Grapes")
                val seed = System.nanoTime()
            }
            withContext(Dispatchers.Main) {
                fruitList!!.setValue(fruitsStringList)
            }
        }
    }

    fun sortVal(int: Int) {
        fruitsStringList.removeAt(int)
        fruitList!!.value = fruitsStringList
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "on cleared called")
    }
}