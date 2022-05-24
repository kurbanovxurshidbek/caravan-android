package com.caravan.caravan.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    lateinit var detailsBinding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsBinding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(detailsBinding.root)

    }
}