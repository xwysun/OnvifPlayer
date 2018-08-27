package com.xwysun.onvifplayer.ui.main

import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwysun.onvifplayer.R
import kotlinx.android.synthetic.main.dialog_login.*
import kotlinx.android.synthetic.main.dialog_login.view.*

/**
 * Author: xwysun
 * Date:2018/8/20
 * Description:
 */
class LoginDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var callback:(String, String)->Unit

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view=inflater!!.inflate(R.layout.dialog_login,container,false)
        view.btnLogin.setOnClickListener {
            callback(account.text.toString(),password.text.toString())
            dismiss()
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isCancelable=true
        val window=dialog.window
        window.setGravity(Gravity.CENTER)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}
