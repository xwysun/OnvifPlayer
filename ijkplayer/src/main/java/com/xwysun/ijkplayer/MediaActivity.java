package com.xwysun.ijkplayer;

import android.content.res.Configuration;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.xwysun.ijkplayer.media.AndroidMediaController;
import com.xwysun.ijkplayer.media.IjkVideoView;

import androidx.viewpager.widget.PagerAdapter;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MediaActivity extends AppCompatActivity {

    IjkVideoView videoView;
    TableLayout hud_view;
    Toolbar toolbar;
    String url;
    public static final String INTENT_DATA="rtsp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_activity);
        videoView=(IjkVideoView) findViewById(R.id.video_view);
        hud_view=(TableLayout) findViewById(R.id.hud_view);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
//        url="rtsp://119.39.49.116:554/ch00000090990000001075.sdp?vcdnid=001";
        url=getIntent().getStringExtra(INTENT_DATA);
        if (url != null) {
            videoView.setVideoURI(Uri.parse(url));
            videoView.setHudView(hud_view);
            final AndroidMediaController controller=new AndroidMediaController(this, false);
            videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer mp) {
                    controller.setAnchorView(videoView);
                    videoView.setMediaController(controller);
                    videoView.start();
                }
            });
        }else {
            Toast.makeText(this, "url为空", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
        videoView.release(false);
    }


}
