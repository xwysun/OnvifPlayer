package com.xwysun.onvifplayer.ui.main

import android.os.Bundle
import com.xwysun.onvifplayer.R
import com.xwysun.onvifplayer.support.adapter.setUP
import com.xwysun.onvifplayer.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.info
import org.jetbrains.anko.toast


class MainActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_search.setOnClickListener({
            toast("test")
        })
    }
}

