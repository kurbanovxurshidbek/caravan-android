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
        val profileId = intent.getStringExtra("profileId")
        val isEdit = intent.getBooleanExtra("isEdit",false)
        val bundle = Bundle()
        bundle.putString("profileId", profileId)
        navController = findNavController(R.id.editNavController)
        navController.setGraph(R.navigation.edit_navigation, bundle)
        if (!isEdit) navController.navigate(R.id.action_editProfileFragment_to_languageFragment)
        initViews()
    }

    private fun initViews() {

    }
}