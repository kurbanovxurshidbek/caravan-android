package com.caravan.caravan.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.caravan.caravan.R

class EditActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val isEdit = intent.getBooleanExtra("isEdit",false)
        val bundle = Bundle()
        navController = findNavController(R.id.editNavController)

        if (isEdit)  navController.navigate(R.id.editProfileFragment)
            else navController.navigate(R.id.action_editProfileFragment_to_languageFragment)
        initViews()
    }

    private fun initViews() {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}