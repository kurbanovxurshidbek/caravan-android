package com.caravan.caravan.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.caravan.caravan.R
import com.caravan.caravan.databinding.AlertDialogBinding
import com.caravan.caravan.databinding.DialogMessageBinding
import com.caravan.caravan.databinding.DialogWarningBinding

object Dialog {

    fun showAlertDialog(context: Context, title: String, handler: OkWithCancelInterface) {
        val dialog = Dialog(context)
        val alertDialogBinding = AlertDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(alertDialogBinding.root)

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
        photo: String,
        description: String,
        ok: OkInterface
    ) {
        val dialog = Dialog(context)
        val msgDialogBinding = DialogMessageBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(msgDialogBinding.root)

        msgDialogBinding.tvTitle.text = title
        Glide.with(context).load(photo).into(msgDialogBinding.imageView)
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
}

interface OkWithCancelInterface {
    fun onOkClick()
    fun onCancelClick()
}

interface OkInterface {
    fun onClick()
}
