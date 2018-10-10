package com.github.spinel.sidebardemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        sideBar.initials = (arrayOf("A", "B", "C", "D", "E", "F", "春", "夏", "秋", "冬", "E", "F", "#"))
        sideBar.hint = hint
        sideBar.callback = { pos, initial -> Log.i("info", "$pos -> $initial") }
    }
}
