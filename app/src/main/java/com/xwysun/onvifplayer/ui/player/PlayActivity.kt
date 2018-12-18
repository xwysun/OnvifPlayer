package com.xwysun.onvifplayer.ui.player

import android.os.Bundle
import android.view.MenuItem
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.xwysun.onvifplayer.R
import com.xwysun.onvifplayer.base.BaseActivity
import com.xwysun.onvifplayer.support.adapter.logd
import com.xwysun.onvifplayer.support.player.JZMediaIjkplayer
import kotlinx.android.synthetic.main.directly_play_activity.*

class PlayActivity :BaseActivity() {

    companion object {
        val INTENT_TAG = "url"
    }

    private lateinit var url:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.directly_play_activity)
        url = intent.getStringExtra(INTENT_TAG)
        logd(url)
        JzvdStd.setMediaInterface(JZMediaIjkplayer())
        jz_video.setUp(JZDataSource(url),JzvdStd.SCREEN_WINDOW_NORMAL)
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}