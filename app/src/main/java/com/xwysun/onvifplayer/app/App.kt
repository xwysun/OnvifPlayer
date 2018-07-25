package com.xwysun.onvifplayer.app

import android.app.Application
/**
 * Author: xwysun
 * Date:2018/7/25
 * Description:
 */
class App : Application() {

    companion object {
        lateinit var instance :App
    }
    override fun onCreate() {
        super.onCreate()
        instance=this
    }
}