package com.xwysun.onvifplayer.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProviders
import com.rvirin.onvif.onvifcamera.*
import com.xwysun.onvifplayer.R
import com.xwysun.onvifplayer.R.id.*
import com.xwysun.onvifplayer.base.BaseAdapter
import com.xwysun.onvifplayer.base.BaseFragment
import com.xwysun.onvifplayer.support.finder.CameraDevice
import com.xwysun.onvifplayer.ui.main.LoginDialog
import com.xwysun.onvifplayer.ui.player.DirectPlayActivity
import kotlinx.android.synthetic.main.item_device.view.*
import kotlinx.android.synthetic.main.scan_fragment.*
import org.jetbrains.anko.debug
import org.jetbrains.anko.toast
import java.net.URL


class ScanFragment: BaseFragment(),OnvifListener {

    private lateinit var  model: ScanViewModel

    private val devices= arrayListOf<CameraDevice>()

    companion object {
        fun newInstance():ScanFragment{
            val fragment=ScanFragment()
            val args=Bundle()
            fragment.arguments=args
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.scan_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model=activity?.run {
            ViewModelProviders.of(this).get(ScanViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        initView()
    }

    override fun onPause() {
        super.onPause()
        model.stopScan()
    }

    private fun initView(){
        val animation=AnimationUtils.loadAnimation(context, R.anim.anim_rotate)
        topBar.setTitle("扫描")
        rv_camera.layoutManager= androidx.recyclerview.widget.LinearLayoutManager(context)
        rv_camera.adapter=mAdapter
        btn_scan.setOnClickListener {
            if (model.isScaning()) {
                model.stopScan()
                animation.cancel()
            }else{
                if (model.startScan {
                            if (it !in mDevices){
                                mDevices.add(it)
                                activity!!.runOnUiThread {
                                    mAdapter.notifyDataSetChanged()
                                }
                            }
                        }){
                    it.startAnimation(animation)
                }
                else{
                    activity!!.toast("设备忙，请稍后再试")
                    model.stopScan()
                    animation.cancel()
                }
            }
        }
    }

    private val mDevices :MutableList<CameraDevice> = mutableListOf()

    private val mAdapter : BaseAdapter<CameraDevice> = BaseAdapter(R.layout.item_device, mDevices) { view, device ->
        view.uuid.text = device.uuid.toString()
        view.url.text = device.serviceURL.toString()
        view.setOnClickListener {
            showLoginDialog(device)
        }
    }
    private fun showLoginDialog(device: CameraDevice){
        val loginDialog= LoginDialog()
        loginDialog.callback={
            account,password->
            connect(device,account,password)
        }
        loginDialog.show(fragmentManager,"login")
    }

    private fun connect(cameraDevice: CameraDevice,account:String,password:String) {
        // If we were able to retrieve information from the camera, and if we have a rtsp uri,
        // We open StreamActivity and pass the rtsp URI
        if (currentDevice.isConnected) {
            currentDevice.rtspURI?.let { uri ->
                val intent= Intent(activity, DirectPlayActivity::class.java);
                intent.putExtra(DirectPlayActivity.INTENT_TAG,uri)
                startActivity(intent)
            } ?: run {
                activity!!.toast("RTSP URI haven't been retrieved")
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
            activity!!.toast("⛔️ Request failed: ${response.request.type}")
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
                    activity!!.toast("$profilesCount profiles retrieved 😎")
                }
                OnvifRequest.Type.GetStreamURI->{
                    activity!!.toast("Stream URI retrieved,\nready for the movie 🍿")
                    currentDevice.rtspURI?.let { uri ->
                        Log.d("uri",uri)
                        val intent= Intent(activity, DirectPlayActivity::class.java);
                        intent.putExtra(DirectPlayActivity.INTENT_TAG,uri)
                        startActivity(intent)
                    } ?: run {
                        activity!!.toast("RTSP URI haven't been retrieved")
                    }
                }
            }
        }
    }


}
