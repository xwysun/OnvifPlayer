package com.xwysun.onvifplayer.ui.home

import androidx.lifecycle.ViewModel
import com.xwymodule.onvif.OnvifDevice
import com.xwymodule.onvif.db.OnvifDao
import com.xwymodule.onvif.db.OnvifDatabase
import com.xwymodule.onvif.finder.OnvifFinder
import com.xwysun.onvifplayer.app.App

class HomeViewModel:ViewModel() {

    private val onvifFinder = OnvifFinder

    private var onvifDao:OnvifDao?= OnvifDatabase.createDb(App.instance)?.onvifDao()

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
        onvifDao?.delete(device)
    }

    fun clearAllDevices(){
        onvifDao?.deleteAll()
    }

    fun saveOnvifDevice(device: OnvifDevice){
        onvifDao?.insert(device)
    }

    fun loadSavedDevices():List<OnvifDevice>?{
        return onvifDao?.getAll()
    }

    fun closeDb(){
        OnvifDatabase.closeDb()
    }

}