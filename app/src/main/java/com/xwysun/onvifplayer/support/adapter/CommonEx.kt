package com.xwysun.onvifplayer.support.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by hupihuai on 2018/7/27.
 */
infix fun ViewGroup.inflate(layoutResId: Int): View =
        LayoutInflater.from(context).inflate(layoutResId, this, false)