package com.tenmafrank.tipcalculator

import android.content.Context
import android.widget.Toast

class Toaster(private var context: Context) {
    fun MakeAToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}