package com.example.myapplication.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View

object TimeUtils {


    fun numToDuration(num: String): String {
        var minsNum = 0

        try {
            minsNum = num.toFloat().toInt()
        } catch(e: NumberFormatException) {
            return "0 min"
        }


        var computedHours = (minsNum / 60)
        var computedMins = (minsNum - (computedHours * 60))

        var ret = ""
        if ( computedHours == 0 ) {
            if ( computedMins <= 1 ) {
                ret = "${computedMins}min"
            } else {
                ret = "${computedMins}mins"
            }
        } else {

            if ( computedHours == 1 ) {
                ret = "${computedHours}hr "
            }
            if ( computedHours > 1 ) {
                ret = "${computedHours}hrs "
            }
            if ( computedMins == 1 ) {
                ret += "${computedMins}min"
            }
            if ( computedMins > 1 ) {
                ret += "${computedMins}mins"
            }

        }

        return ret
    }

}