package com.dung.demoloadmorefirebase

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.add_dialog.*

open class AddDialog(val activity: Activity, val onClickItemListener: OnClickItemListener) : Dialog(activity),
    View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_dialog)

        btnAdd.setOnClickListener(this)
        btnHuy.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnAdd -> {
                val name = edtName.text.toString()
                onClickItemListener.onCLickItem(name)
            }
            R.id.btnHuy -> {
                cancel()
            }
        }
    }

    interface OnClickItemListener {
        fun onCLickItem(name: String)
    }
}