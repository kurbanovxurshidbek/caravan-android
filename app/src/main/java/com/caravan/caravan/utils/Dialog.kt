package com.caravan.caravan.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.databinding.AlertDialogBinding
import com.caravan.caravan.databinding.DialogLoadingBinding
import com.caravan.caravan.databinding.DialogMessageBinding
import com.caravan.caravan.databinding.DialogWarningBinding

object Dialog {

    private var loadingDialog: Dialog? = null

    fun showAlertDialog(context: Context, title: String, handler: OkWithCancelInterface) {
        val dialog = Dialog(context)
        val alertDialogBinding = AlertDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(alertDialogBinding.root)
        dialog.setCancelable(false)

        alertDialogBinding.tvTitle.text = title
        alertDialogBinding.btnYes.setOnClickListener {
            handler.onOkClick()
            dialog.dismiss()
        }

        alertDialogBinding.btnNo.setOnClickListener {
            handler.onCancelClick()
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(context.getDrawable(R.drawable.ic_launcher_foreground))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    fun showDialogMessage(
        context: Context,
        title: String,
        description: String,
        ok: OkInterface
    ) {
        val dialog = Dialog(context)
        val msgDialogBinding = DialogMessageBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(msgDialogBinding.root)
        dialog.setCancelable(false)

        msgDialogBinding.tvTitle.text = title
        msgDialogBinding.tvMessage.text = description
        msgDialogBinding.btnOk.setOnClickListener {
            ok.onClick()
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(context.getDrawable(R.drawable.ic_launcher_foreground))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    fun showDialogWarning(context: Context, title: String, description: String, ok: OkInterface) {
        val dialog = Dialog(context)
        val warningBinding = DialogWarningBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(warningBinding.root)
        dialog.setCancelable(false)

        warningBinding.tvTitle.text = title
        warningBinding.tvMessage.text = description

        warningBinding.btnOk.setOnClickListener {
            ok.onClick()
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(context.getDrawable(R.drawable.ic_launcher_foreground))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    fun showLoading(context: Context) {
        if (loadingDialog == null)
            loadingDialog = Dialog(context)
        val loadingBinding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
        loadingDialog?.setContentView(loadingBinding.root)
        loadingDialog?.setCancelable(false)

        loadingDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingDialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        loadingDialog?.show()

    }

    fun dismissLoading() {
        loadingDialog?.dismiss()
    }

}

interface OkWithCancelInterface {
    fun onOkClick()
    fun onCancelClick()
}

interface OkInterface {
    fun onClick()
}
