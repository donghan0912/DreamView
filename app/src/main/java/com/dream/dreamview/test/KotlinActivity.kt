package com.dream.dreamview.test

import android.os.Bundle
import android.widget.TextView
import com.dream.dreamview.R
import com.dream.dreamview.base.NavBaseActivity

class KotlinActivity : NavBaseActivity() {
    override fun getContentView(): Int {
        return R.layout.kotlin_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var textView = findViewById<TextView>(R.id.text)
    }
}
