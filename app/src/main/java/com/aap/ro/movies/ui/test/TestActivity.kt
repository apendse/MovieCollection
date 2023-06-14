package com.aap.ro.movies.ui.test

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aap.ro.movies.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }
    override fun onResume() {
        super.onResume()
        Log.d("YYYY", "TestActivity onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("YYYY", "TestActivity stopped")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("YYYY", "TestActivity destroyed")
    }

}