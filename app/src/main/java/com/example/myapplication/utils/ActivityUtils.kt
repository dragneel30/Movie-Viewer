package com.example.myapplication.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View

object ActivityUtils {


    fun navigate(context: Context, cls: Class<*>) {
        var intent =  Intent(context, cls)
        context.startActivity(intent)
    }

    fun navigate(context: Context, cls: Class<*>, bundle: Bundle) {
        var intent =  Intent(context, cls)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

}