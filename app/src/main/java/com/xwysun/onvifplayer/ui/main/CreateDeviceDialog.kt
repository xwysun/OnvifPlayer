package com.xwysun.onvifplayer.ui.main

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.xwysun.onvifplayer.R
import kotlinx.android.synthetic.main.dialog_create.*

/**
 * Author: xwysun
 * Date:2018/8/20
 * Description:
 */
class CreateDeviceDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var callback:(String,String, String)->Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.dialog_create,container,false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isCancelable=true
        val window=dialog.window
        window.setGravity(Gravity.CENTER)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        btnSave.setOnClickListener {
            callback(ip.text.toString(),account.text.toString(),password.text.toString())
            dismiss()
        }
    }

}
