package com.xwysun.ijkplayer;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
