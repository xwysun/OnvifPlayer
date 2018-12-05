package com.xwysun.onvifplayer.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xwysun.onvifplayer.support.finder.CameraDevice

class ScanViewModel:ViewModel() {

    private val devices=MutableLiveData<CameraDevice>()
        get()=field

}