//package com.xwysun.onvifplayer.ui.stream
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.View
//import android.widget.Toast
//import com.pedro.vlc.VlcListener
//import com.pedro.vlc.VlcVideoLibrary
//import com.xwysun.onvifplayer.R
//import kotlinx.android.synthetic.main.activity_stream.*
//
//const val RTSP_URL = "com.rvirin.onvif.onvifcamera.demo.RTSP_URL"
///**
// * This activity helps us to show the live stream of an ONVIF camera thanks to VLC library.
// */
//class StreamActivity : AppCompatActivity(), VlcListener, View.OnClickListener {
//
//
//    private var vlcVideoLibrary: VlcVideoLibrary? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_stream)
//        btnPlay.setOnClickListener(this)
//        vlcVideoLibrary = VlcVideoLibrary(this, this, surfaceView)
//    }
//
//    /**
//     * Called by VLC library when the video is loading
//     */
//    override fun onComplete() {
//        Toast.makeText(this, "Loading video...", Toast.LENGTH_LONG).show()
//    }
//
//    /**
//     * Called by VLC library when an error occured (most of the time, a problem in the URI)
//     */
//    override fun onError() {
//        Toast.makeText(this, "Error, make sure your endpoint is correct", Toast.LENGTH_SHORT).show()
//        vlcVideoLibrary?.stop()
//    }
//
//
//    override fun onClick(v: View?) {
//
//        vlcVideoLibrary?.let { vlcVideoLibrary ->
//
//            if (!vlcVideoLibrary.isPlaying) {
//                val url = intent.getStringExtra(RTSP_URL)
//                vlcVideoLibrary.play(url)
//
//            } else {
//                vlcVideoLibrary.stop()
//
//            }
//        }
//    }
//}
//
