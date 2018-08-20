package com.xwysun.onvifplayer.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.rvirin.onvif.onvifcamera.*
import com.xwysun.onvifplayer.R
import com.xwysun.onvifplayer.support.finder.CameraDevice
import com.xwysun.onvifplayer.support.finder.CameraFinder
import com.xwysun.onvifplayer.base.BaseActivity
import com.xwysun.onvifplayer.base.BaseAdapter
import com.xwysun.onvifplayer.ui.stream.RTSP_URL
import com.xwysun.onvifplayer.ui.stream.StreamActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_device.view.*
import org.jetbrains.anko.debug
import org.jetbrains.anko.error
import org.jetbrains.anko.toast


class MainActivity : BaseActivity(), OnvifListener {

    private val mFinder :CameraFinder by lazy {
        CameraFinder(this)
    }

    private val mDevices :MutableList<CameraDevice> = mutableListOf()

    private val mAdapter : BaseAdapter<CameraDevice> = BaseAdapter(R.layout.item_device, mDevices) { view, device ->
        view.uuid.text = device.uuid.toString()
        view.url.text = device.serviceURL.toString()
        view.setOnClickListener {
            connect(device)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_search.setOnClickListener {
            mFinder.start()
        }
        rv_camera.layoutManager=LinearLayoutManager(this)
        rv_camera.adapter=mAdapter
        init()
    }

    private fun init(){
       mFinder.setOnCameraFinderListener {
           if (it !in mDevices) mDevices.add(it)
           mAdapter.notifyDataSetChanged()
       }
    }

    override fun onPause() {
        super.onPause()
        try { mFinder.stop() }catch (e :InterruptedException){error { e.message }}
    }


    private fun connect(cameraDevice: CameraDevice) {
        // If we were able to retrieve information from the camera, and if we have a rtsp uri,
        // We open StreamActivity and pass the rtsp URI
        if (currentDevice.isConnected) {
            currentDevice.rtspURI?.let { uri ->
                val intent = Intent(this, StreamActivity::class.java).apply {
                    putExtra(RTSP_URL, uri)
                }
                startActivity(intent)
            } ?: run {
                Toast.makeText(this, "RTSP URI haven't been retrieved", Toast.LENGTH_SHORT).show()
            }
        } else {
            currentDevice = OnvifDevice(cameraDevice.serviceURL, "admin", "admin")
            currentDevice.listener = this
            currentDevice.getServices()
        }
    }


    override fun requestPerformed(response: OnvifResponse) {

        Log.d("INFO", response.parsingUIMessage)

        if (!response.success) {
            Log.e("ERROR", "request failed: ${response.request.type} \n Response: ${response.error}")
            toast("⛔️ Request failed: ${response.request.type}")
        }
        // if GetServices have been completed, we request the device information
        else if (response.request.type == OnvifRequest.Type.GetServices) {
            currentDevice.getDeviceInformation()
        }
        // if GetDeviceInformation have been completed, we request the profiles
        else if (response.request.type == OnvifRequest.Type.GetDeviceInformation) {
            toast("Device information retrieved 👍")
            debug { "DeviceInfo:${response.parsingUIMessage}"}
            currentDevice.getProfiles()

        }
        // if GetProfiles have been completed, we request the Stream URI
        else if (response.request.type == OnvifRequest.Type.GetProfiles) {
            val profilesCount = currentDevice.mediaProfiles.count()
            currentDevice.getStreamURI()
            toast("$profilesCount profiles retrieved 😎")
        }
        // if GetStreamURI have been completed, we're ready to play the video
        else if (response.request.type == OnvifRequest.Type.GetStreamURI) {
            toast("Stream URI retrieved,\nready for the movie 🍿")
        }
    }

}

