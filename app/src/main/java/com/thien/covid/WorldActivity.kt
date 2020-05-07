package com.thien.covid

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_world.*
import kotlinx.android.synthetic.main.item.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class WorldActivity : AppCompatActivity() {

    var isList1ON = true
    var isList2ON = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_world)

        val a = AnimationUtils.loadAnimation(this, R.anim.bounce_in)
        w_title.startAnimation(a)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        w_list.layoutManager = layoutManager
        val layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        w_list2.layoutManager = layoutManager2

        if (isList1ON) {
            w_list.visibility = View.VISIBLE
            w_text1.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_drop_up,
                0
            )
        } else {
            w_list.visibility = View.GONE
            w_text1.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_drop_down,
                0
            )
        }

        if (isList2ON) {
            w_list2.visibility = View.VISIBLE
            w_text2.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_drop_up,
                0
            )
        } else {
            w_list2.visibility = View.GONE
            w_text2.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_drop_down,
                0
            )
        }

        w_text1.setOnClickListener {
            if (!isList1ON) {
                w_list.visibility = View.VISIBLE
                isList1ON = true
                w_text1.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_drop_up,
                    0
                )
            } else {
                w_list.visibility = View.GONE
                isList1ON = false
                w_text1.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_drop_down,
                    0
                )
            }
        }

        w_text2.setOnClickListener {
            if (!isList2ON) {
                w_list2.visibility = View.VISIBLE
                isList2ON = true
                w_text2.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_drop_up,
                    0
                )
            } else {
                w_list2.visibility = View.GONE
                isList2ON = false
                w_text2.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_drop_down,
                    0
                )
            }
        }

        val data = intent.getSerializableExtra("DATADATA") as ArrayList<W>
        val data1 = ArrayList<W>()
        val data2 = ArrayList<W>()
        for (m in data) {
            if (m.country_vn == "Châu Á"
                || m.country_vn == "Châu Âu"
                || m.country_vn == "Châu Phi"
                || m.country_vn == "Bắc Mỹ"
                || m.country_vn == "Nam Mỹ"
                || m.country_vn == "Oceania"
            ) {
                data1.add(m)
            } else {
                data2.add(m)
            }
        }

        val adapter = GroupAdapter<ViewHolder>()
        for (m in data1) {
            if (m.country_vn == "Oceania") m.country_vn = "Châu Úc"
            adapter.add(ItemW(m))
        }
        w_list.adapter = adapter

        val adapter2 = GroupAdapter<ViewHolder>()
        for (m in data2) {
            adapter2.add(ItemW(m))
        }
        w_list2.adapter = adapter2
    }
}

class ItemW(private val w: W) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item2
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.ip_name.text = w.country_vn

        if (w.cases == "") viewHolder.itemView.ip_case.text = "0"
        if (w.recovered == "") viewHolder.itemView.ip_recover.text = "0"
        if (w.deaths == "") viewHolder.itemView.ip_death.text = "0"

        try {
            viewHolder.itemView.ip_case.text =
                NumberFormat.getNumberInstance(Locale.US).format(w.cases.toInt())
            viewHolder.itemView.ip_recover.text =
                NumberFormat.getNumberInstance(Locale.US).format(w.recovered.toInt())
            viewHolder.itemView.ip_death.text =
                NumberFormat.getNumberInstance(Locale.US).format(w.deaths.toInt())
        } catch (e: Exception) {
            Log.d("error", e.toString())
        }
    }
}