package com.xwysun.onvifplayer.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.xwysun.onvifplayer.R
import com.xwysun.onvifplayer.support.adapter.setUP
import com.xwysun.onvifplayer.support.finder.CameraFinder
import com.xwysun.onvifplayer.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.info
import org.jetbrains.anko.toast


class MainActivity : BaseActivity(){


    lateinit var mFinder :CameraFinder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_search.setOnClickListener({
            toast("test")
        })
        init()
    }

    fun init(){
        mFinder=CameraFinder(this)
        mFinder.getmCameraList().observe(this, Observer {
            it?.let {
                info("size"+it.size)
            }
        })
    }

}

