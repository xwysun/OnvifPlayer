package com.xwysun.onvifplayer.support.adapter

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Created by hupihuai on 2018/7/27.
 */
infix fun ViewGroup.inflate(layoutResId: Int): View =
        LayoutInflater.from(context).inflate(layoutResId, this, false)

fun Activity.toast(text:String,length:Int=Toast.LENGTH_SHORT){
    Toast.makeText(this,text,length).show()
}

fun Fragment.toast(text:String,length:Int=Toast.LENGTH_SHORT){
    Toast.makeText(activity,text,length).show()
}

inline fun Any.logd(msg:String){
    Log.d("TAG_" + this::class.java.simpleName, msg)
}
inline fun Any.loge(msg:String){
    Log.e("TAG_" + this::class.java.simpleName, msg)
}
inline fun Any.logi(msg:String){
    Log.i("TAG_" + this::class.java.simpleName, msg)
}

