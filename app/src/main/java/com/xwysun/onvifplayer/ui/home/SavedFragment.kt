package com.xwysun.onvifplayer.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwymodule.onvif.*
import com.xwysun.onvifplayer.R
import com.xwysun.onvifplayer.base.BaseAdapter
import com.xwysun.onvifplayer.ui.main.CreateDeviceDialog
import com.xwysun.onvifplayer.ui.player.DirectPlayActivity
import kotlinx.android.synthetic.main.item_device.view.*
import kotlinx.android.synthetic.main.saved_fragment.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.toast

class SavedFragment :Fragment(), OnvifListener {

    private lateinit var  mViewModel: HomeViewModel

    private val devices= arrayListOf<OnvifDevice>()

    private val mAdapter : BaseAdapter<OnvifDevice> = BaseAdapter(R.layout.item_device, devices) { view, device ->
        view.uuid.text = device.uuid.toString()
        view.url.text = device.ipAddress
        view.setOnClickListener {
           connect(device)
        }
    }

    companion object {
        fun newInstance():SavedFragment{
            val fragment=SavedFragment()
            val args= Bundle()
            fragment.arguments=args
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.saved_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel=activity?.run {
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        initTopBar()
        initView()
    }
    private fun initTopBar(){
        topBar.setTitle("已保存设备")
        topBar.addRightImageButton(R.mipmap.ic_camera_add,R.id.add_camera).setOnClickListener {
            showAddDialog()
        }
    }

    private fun initView(){
        srf_saved.setOnRefreshListener {
            refreshData()
            srf_saved.isRefreshing=false
        }
        rv_camera.layoutManager= LinearLayoutManager(context)
        rv_camera.adapter=mAdapter
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun showAddDialog(){
        val createDeviceDialog= CreateDeviceDialog()
        createDeviceDialog.callback={
            ip,account,password->
            val onvifDevice=OnvifDevice(ip)
            onvifDevice.username=account
            onvifDevice.password=password
            launch(UI){
                val task= async(CommonPool){
                    mViewModel.saveOnvifDevice(onvifDevice)
                }
                task.await()
                refreshData()
            }
        }
        createDeviceDialog.show(fragmentManager,"login")
    }

    private fun refreshData()= launch(UI){
        val task= async(CommonPool){
            devices.clear()
            devices.addAll(mViewModel.loadSavedDevices()!!)
        }
        task.await()
        activity!!.runOnUiThread {
            mAdapter.notifyDataSetChanged()
        }
    }
    private fun connect(onvifDevice: OnvifDevice) {
        // If we were able to retrieve information from the camera, and if we have a rtsp uri,
        // We open StreamActivity and pass the rtsp URI
        currentDevice =onvifDevice
        if (currentDevice.isConnected) {
            currentDevice.rtspURI?.let { uri ->
                val intent= Intent(activity, DirectPlayActivity::class.java);
                intent.putExtra(DirectPlayActivity.INTENT_TAG,uri)
                startActivity(intent)
            } ?: run {
                activity!!.toast("RTSP URI haven't been retrieved")
            }
        } else {
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