package com.xwysun.onvifplayer.ui.player

import android.view.MotionEvent
import cn.jzvd.Jzvd.SCREEN_WINDOW_FULLSCREEN
import android.R.attr.onClick
import android.content.Context
import android.util.AttributeSet
import android.view.View
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.xwysun.onvifplayer.R


class MyJzvdStd : JzvdStd {

    constructor(context: Context):super(context)

    constructor(context: Context, attrs: AttributeSet):super(context, attrs)

    override fun init(context: Context) {
        super.init(context)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        val i = v.id
        if (i == cn.jzvd.R.id.fullscreen) {
            if (currentScreen == Jzvd.SCREEN_WINDOW_FULLSCREEN) {
                //click quit fullscreen
            } else {
                //click goto fullscreen
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.jz_layout_std
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return super.onTouch(v, event)
    }

    override fun startVideo() {
        super.startVideo()
    }

    //onState 代表了播放器引擎的回调，播放视频各个过程的状态的回调
    override fun onStateNormal() {
        super.onStateNormal()
    }

    override fun onStatePreparing() {
        super.onStatePreparing()
    }

    override fun onStatePlaying() {
        super.onStatePlaying()
    }

    override fun onStatePause() {
        super.onStatePause()
    }

    override fun onStateError() {
        super.onStateError()
    }

    override fun onStateAutoComplete() {
        super.onStateAutoComplete()
    }

    //changeUiTo 真能能修改ui的方法
    override fun changeUiToNormal() {
        super.changeUiToNormal()
    }

    override fun changeUiToPreparing() {
        super.changeUiToPreparing()
    }

    override fun changeUiToPlayingShow() {
        super.changeUiToPlayingShow()
    }

    override fun changeUiToPlayingClear() {
        super.changeUiToPlayingClear()
    }

    override fun changeUiToPauseShow() {
        super.changeUiToPauseShow()
    }

    override fun changeUiToPauseClear() {
        super.changeUiToPauseClear()
    }

    override fun changeUiToComplete() {
        super.changeUiToComplete()
    }

    override fun changeUiToError() {
        super.changeUiToError()
    }

    override fun onInfo(what: Int, extra: Int) {
        super.onInfo(what, extra)
    }

    override fun onError(what: Int, extra: Int) {
        super.onError(what, extra)
    }

    override fun startWindowFullscreen() {
        super.startWindowFullscreen()
    }

    override fun startWindowTiny() {
        super.startWindowTiny()
    }

}