package com.xwysun.onvifplayer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.xwymodule.onvif.OnvifDevice
import com.xwysun.onvifplayer.R
import com.xwysun.onvifplayer.base.BaseAdapter
import kotlinx.android.synthetic.main.item_device.view.*
import kotlinx.android.synthetic.main.saved_fragment.*

class SavedFragment :Fragment(){

    private lateinit var  mViewModel: HomeViewModel

    private val devices= arrayListOf<OnvifDevice>()

    private val mAdapter : BaseAdapter<OnvifDevice> = BaseAdapter(R.layout.item_device, devices) { view, device ->
        view.uuid.text = device.uuid.toString()
        view.url.text = device.ipAddress
        view.setOnClickListener {
           //todo
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
    }
    private fun initTopBar(){
        topBar.setTitle("已保存设备")
        topBar.addRightImageButton(R.mipmap.ic_camera_add,R.id.add_camera)
        //todo
    }

    private fun initView(){
        srf_saved.setOnRefreshListener {
            //todo
            srf_saved.isRefreshing=false
        }
        rv_camera.layoutManager= androidx.recyclerview.widget.LinearLayoutManager(context)
        rv_camera.adapter=mAdapter
    }

    private fun initObserver(){

    }

}