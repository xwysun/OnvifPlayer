package com.xwysun.onvifplayer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwysun.onvifplayer.R
import com.xwysun.onvifplayer.base.BaseAdapter
import com.xwysun.onvifplayer.support.finder.CameraDevice
import com.xwysun.onvifplayer.ui.main.LoginDialog
import kotlinx.android.synthetic.main.item_device.view.*
import kotlinx.android.synthetic.main.scan_fragment.*

class ScanFragment: Fragment() {

    companion object {
        fun newInstance():ScanFragment{
            val fragment=ScanFragment()
            val args=Bundle()
            fragment.arguments=args
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.scan_fragment,container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_search.setOnClickListener {

        }
        rv_camera.layoutManager= androidx.recyclerview.widget.LinearLayoutManager(context)
        rv_camera.adapter=mAdapter
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
        val loginDialog= LoginDialog()
        loginDialog.callback={
            account,password->
//            connect(device,account,password)
        }
        loginDialog.show(fragmentManager,"login")
    }


}
