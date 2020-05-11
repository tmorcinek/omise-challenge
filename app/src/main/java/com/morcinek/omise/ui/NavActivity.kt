package com.morcinek.omise.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.morcinek.omise.R
import kotlinx.android.synthetic.main.activity_nav.*

class NavActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(this, findNavController(this, R.id.navHostFragment))
    }
}

fun Fragment.findNavController() = findNavController(view!!)

fun Fragment.lazyNavController() = lazy { findNavController() }
