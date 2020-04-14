package com.thien.covid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = MyAdapter(supportFragmentManager)
        adapter.add(VFragment(), "Việt Nam")
        adapter.add(WFragment(), "Thế giới")
        adapter.add(IFragment(), "Thông tin")
        adapter.add(NFragment(), "Tin tức")
        adapter.add(CFragment(), "Hỗ trợ")
        main_pager.adapter = adapter
        main_tab.setupWithViewPager(main_pager)
    }
}