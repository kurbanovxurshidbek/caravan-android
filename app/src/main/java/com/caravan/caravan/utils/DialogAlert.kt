package com.caravan.caravan.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.caravan.caravan.R
import com.caravan.caravan.databinding.AlertDialogBinding

class DialogAlert(private val title: String) : DialogFragment() {
    lateinit var yesListener: (() -> Unit)
    lateinit var noListener: (() -> Unit)
    private var _bn: AlertDialogBinding? = null
    private val binding get() = _bn!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bn = AlertDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        binding.apply {
            btnYes.setOnClickListener {
                yesListener.invoke()
            }

            tvTitle.text = title

            btnNo.setOnClickListener {
                noListener.invoke()
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _bn = null
    }
}