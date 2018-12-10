package com.xwysun.onvifplayer.ui.home

import androidx.lifecycle.ViewModel
import com.xwymodule.onvif.OnvifDevice
import com.xwysun.onvifplayer.support.finder.OnvifFinder

class HomeViewModel:ViewModel() {

    private val onvifFinder =OnvifFinder

    fun isScaning():Boolean{
        return onvifFinder.isSearching
    }

    fun startScan(listener:(OnvifDevice)->Unit):Boolean{
       return onvifFinder.start(listener)
    }

    fun stopScan(){
        onvifFinder.stop()
    }

    fun deleteOnvifDevice(device: OnvifDevice){

    }

    fun clearAllDevices(){

    }

    fun saveOnvifDevice(device: OnvifDevice){

    }

    fun loadSavedDevices():List<OnvifDevice>{

        return emptyList()
    }

}