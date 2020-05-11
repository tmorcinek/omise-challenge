package com.morcinek.omise.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.morcinek.omise.R
import kotlinx.android.synthetic.main.activity_nav.*

class NavActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
        setSupportActionBar(toolbar)
    }
}
