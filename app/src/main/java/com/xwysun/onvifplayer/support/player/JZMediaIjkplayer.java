package com.xwysun.onvifplayer.support.player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;

import java.io.IOException;

import cn.jzvd.JZMediaInterface;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JzvdMgr;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * Created by Nathen on 2017/11/18.
 */

public class JZMediaIjkplayer extends JZMediaInterface implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnTimedTextListener {
    IjkMediaPlayer ijkMediaPlayer;

    @Override
    public void start() {
        ijkMediaPlayer.start();
    }

    @Override
    public void prepare() {
        ijkMediaPlayer = new IjkMediaPlayer();
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 1024 * 1024);

        // 支持硬解 1：开启 O:关闭
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-hevc", 1);
        // 设置播放前的探测时间 1,达到首屏秒开效果
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", 1);

        /**
         * 播放延时的解决方案
         */
        // 如果是rtsp协议，可以优先用tcp(默认是用udp)
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");
        // 设置播放前的最大探测时间 （100未测试是否是最佳值）
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100L);
        // 每处理一个packet之后刷新io上下文
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1L);
        // 需要准备好后自动播放
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);
        // 不额外优化（使能非规范兼容优化，默认值0 ）
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fast", 1);
        // 是否开启预缓冲，一般直播项目会开启，达到秒开的效果，不过带来了播放丢帧卡顿的体验
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering",  0);
        // 自动旋屏
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0);
        // 处理分辨率变化
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 0);
        // 最大缓冲大小,单位kb
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "max-buffer-size", 0);
        // 默认最小帧数2
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", 2);
        // 最大缓存时长
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,  "max_cached_duration", 3); //300
        // 是否限制输入缓存数
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,  "infbuf", 1);
        // 缩短播放的rtmp视频延迟在1s内
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "nobuffer");
        // 播放前的探测Size，默认是1M, 改小一点会出画面更快
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 200); //1024L)
        // 播放重连次数
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"reconnect",5);
        // TODO:
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
        // 设置是否开启环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48L);
        // 跳过帧 ？？
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_frame", 0);
        // 视频帧处理不过来的时候丢弃一些帧达到同步的效果
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 5);

        ijkMediaPlayer.setOnPreparedListener(JZMediaIjkplayer.this);
        ijkMediaPlayer.setOnVideoSizeChangedListener(JZMediaIjkplayer.this);
        ijkMediaPlayer.setOnCompletionListener(JZMediaIjkplayer.this);
        ijkMediaPlayer.setOnErrorListener(JZMediaIjkplayer.this);
        ijkMediaPlayer.setOnInfoListener(JZMediaIjkplayer.this);
        ijkMediaPlayer.setOnBufferingUpdateListener(JZMediaIjkplayer.this);
        ijkMediaPlayer.setOnSeekCompleteListener(JZMediaIjkplayer.this);
        ijkMediaPlayer.setOnTimedTextListener(JZMediaIjkplayer.this);

        try {
            ijkMediaPlayer.setDataSource(jzDataSource.getCurrentUrl().toString());
            ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            ijkMediaPlayer.setScreenOnWhilePlaying(true);
            ijkMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        ijkMediaPlayer.pause();
    }

    @Override
    public boolean isPlaying() {
        return ijkMediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long time) {
        ijkMediaPlayer.seekTo(time);
    }

    @Override
    public void release() {
        if (ijkMediaPlayer != null)
            ijkMediaPlayer.release();
    }

    @Override
    public long getCurrentPosition() {
        return ijkMediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return ijkMediaPlayer.getDuration();
    }

    @Override
    public void setSurface(Surface surface) {
        ijkMediaPlayer.setSurface(surface);
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        ijkMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public void setSpeed(float speed) {
        ijkMediaPlayer.setSpeed(speed);
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        ijkMediaPlayer.start();
        if (jzDataSource.getCurrentUrl().toString().toLowerCase().contains("mp3")) {
            JZMediaManager.instance().mainThreadHandler.post(() -> {
                if (JzvdMgr.getCurrentJzvd() != null) {
                    JzvdMgr.getCurrentJzvd().onPrepared();
                }
            });
        }
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
        JZMediaManager.instance().currentVideoWidth = iMediaPlayer.getVideoWidth();
        JZMediaManager.instance().currentVideoHeight = iMediaPlayer.getVideoHeight();
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().onVideoSizeChanged();
            }
        });
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().onAutoCompletion();
            }
        });
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, final int what, final int extra) {
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().onError(what, extra);
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, final int what, final int extra) {
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    JzvdMgr.getCurrentJzvd().onPrepared();
                } else {
                    JzvdMgr.getCurrentJzvd().onInfo(what, extra);
                }
            }
        });
        return false;
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, final int percent) {
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().setBufferProgress(percent);
            }
        });
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().onSeekComplete();
            }
        });
    }

    @Override
    public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {

    }
}
