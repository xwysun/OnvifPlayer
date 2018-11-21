package com.xwysun.onvifplayer.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.rvirin.onvif.onvifcamera.*
import com.xwysun.ijkplayer.MediaActivity
import com.xwysun.onvifplayer.R
import com.xwysun.onvifplayer.support.finder.CameraDevice
import com.xwysun.onvifplayer.support.finder.CameraFinder
import com.xwysun.onvifplayer.base.BaseActivity
import com.xwysun.onvifplayer.base.BaseAdapter
//import com.xwysun.onvifplayer.ui.stream.RTSP_URL
//import com.xwysun.onvifplayer.ui.stream.StreamActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_device.view.*
import org.jetbrains.anko.debug
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.net.URL


class MainActivity : BaseActivity(), OnvifListener {

    private val TEST_URL="rtsp://admin:admin@192.168.2.2:554/1/1"
    private val mFinder :CameraFinder by lazy {
        CameraFinder(this)
    }

    private val mDevices :MutableList<CameraDevice> = mutableListOf()

    private val mAdapter : BaseAdapter<CameraDevice> = BaseAdapter(R.layout.item_device, mDevices) { view, device ->
        view.uuid.text = device.uuid.toString()
        view.url.text = device.serviceURL.toString()
        view.setOnClickListener {
//            connect(device,"admin","admin")
            showLoginDialog(device)
        }
    }
    private fun showLoginDialog(device: CameraDevice){
        val loginDialog=LoginDialog()
        loginDialog.callback={
            account,password->
            connect(device,account,password)
        }
        loginDialog.show(fragmentManager,"login")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_search.setOnClickListener {
            mFinder.start()
        }
        rv_camera.layoutManager= androidx.recyclerview.widget.LinearLayoutManager(this)
        rv_camera.adapter=mAdapter
        init()
    }

    private fun init(){
       mFinder.setOnCameraFinderListener {
           if (it !in mDevices) mDevices.add(it)
           runOnUiThread {
               mAdapter.notifyDataSetChanged()
           }
       }
    }

    override fun onPause() {
        super.onPause()
        try { mFinder.stop() }catch (e :InterruptedException){error { e.message }}
    }


    private fun connect(cameraDevice: CameraDevice,account:String,password:String) {
        // If we were able to retrieve information from the camera, and if we have a rtsp uri,
        // We open StreamActivity and pass the rtsp URI
        if (currentDevice.isConnected) {
            currentDevice.rtspURI?.let { uri ->
                val intent=Intent(this@MainActivity,MediaActivity::class.java)
                intent.putExtra(MediaActivity.INTENT_DATA,uri)
                startActivity(intent)
            } ?: run {
                Toast.makeText(this, "RTSP URI haven't been retrieved", Toast.LENGTH_SHORT).show()
            }
        } else {
            var url= URL(cameraDevice.serviceURL)
            currentDevice = OnvifDevice(url.host, account, password)
            currentDevice.listener = this
            currentDevice.getCapabilities()
        }
    }


    override fun requestPerformed(response: OnvifResponse) {

        Log.d("INFO", response.parsingUIMessage)

        if (!response.success) {
            Log.e("ERROR", "request failed: ${response.request.type} \n Response: ${response.error}")
            toast("⛔️ Request failed: ${response.request.type}")
        }else{
            when(response.request.type){
//                OnvifRequest.Type.GetServices->{
//                    currentDevice.getDeviceInformation()
//                }
//                OnvifRequest.Type.GetDeviceInformation->{
//                    toast("Device information retrieved 👍")
//                    debug { "DeviceInfo:${response.parsingUIMessage}"}
//                }
                OnvifRequest.Type.GetCapabilities->{
                    currentDevice.getProfiles()
                }
                OnvifRequest.Type.GetProfiles->{
                    val profilesCount = currentDevice.mediaProfiles.count()
                    currentDevice.getStreamURI()
                    toast("$profilesCount profiles retrieved 😎")
                }
                OnvifRequest.Type.GetStreamURI->{
                    toast("Stream URI retrieved,\nready for the movie 🍿")
                    currentDevice.rtspURI?.let { uri ->
                        Log.d("uri",uri)
                        val intent=Intent(this@MainActivity,MediaActivity::class.java);
                        intent.putExtra(MediaActivity.INTENT_DATA,uri)
                        runOnUiThread {
                            startActivity(intent)
                        }
                    } ?: run {
                        Toast.makeText(this, "RTSP URI haven't been retrieved", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}

