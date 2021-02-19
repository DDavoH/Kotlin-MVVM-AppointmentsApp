package com.davoh.laravelmyappointments.ui.dialogs

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.davoh.laravelmyappointments.R


class LoadingDialog(private val mActivity: Activity){

    private lateinit var dialog:AlertDialog

    fun startLoadingDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(mActivity)
        val inflater = mActivity.layoutInflater
        builder.setView(inflater.inflate(R.layout.dialog_loading,null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }

}