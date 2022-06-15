package com.caravan.caravan.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.caravan.caravan.R
import com.caravan.caravan.databinding.DialogMessageBinding
import com.caravan.caravan.databinding.DialogWarningBinding

class DialogMessage(private val title: String, private val message: String) : DialogFragment() {
    lateinit var okListener: (() -> Unit)
    private var _bn: DialogMessageBinding? = null
    private val binding get() = _bn!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _bn = DialogMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        binding.apply {
            btnOk.setOnClickListener {
                okListener.invoke()
            }

            tvMessage.text = message
            tvTitle.text = title

        }

    }
    override fun onDestroy() {
        super.onDestroy()
        _bn = null
    }
}