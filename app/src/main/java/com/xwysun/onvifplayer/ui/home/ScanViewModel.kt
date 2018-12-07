package com.xwysun.onvifplayer.ui.home

import androidx.lifecycle.ViewModel
import com.xwysun.onvifplayer.support.finder.CameraDevice
import com.xwysun.onvifplayer.support.finder.OnvifFinder

class ScanViewModel:ViewModel() {

    private val onvifFinder =OnvifFinder

    fun isScaning():Boolean{
        return onvifFinder.isSearching
    }

    fun startScan(listener:(CameraDevice)->Unit):Boolean{
       return onvifFinder.start(listener)
    }

    fun stopScan(){
        onvifFinder.stop()
    }

}