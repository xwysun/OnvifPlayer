package com.xwysun.onvifplayer.ui.player;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.xwysun.onvifplayer.R;
import com.xwysun.onvifplayer.support.player.JZMediaIjkplayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class DirectPlayActivity extends AppCompatActivity {

    public static final String INTENT_TAG="url";
    private String url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directly_play_activity);
        url=getIntent().getStringExtra(INTENT_TAG);
        JzvdStd.setMediaInterface(new JZMediaIjkplayer());
        JzvdStd.startFullscreen(this, JzvdStd.class, url, null);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickFullScreen(View view) {
        JzvdStd.startFullscreen(this, JzvdStd.class, url, null);
    }
}
