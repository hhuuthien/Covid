package com.thien.covid

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_world.*
import kotlinx.android.synthetic.main.item2.view.*
import kotlinx.android.synthetic.main.item5.view.*
import java.lang.Integer.parseInt
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class WorldActivity : AppCompatActivity() {

    private val data1 = ArrayList<W>()
    private val data2 = ArrayList<W>()
    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_world)

        val a = AnimationUtils.loadAnimation(this, R.anim.bounce_in)
        w_title.startAnimation(a)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        w_list.layoutManager = layoutManager
        w_list.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))

        val data = intent.getSerializableExtra("DATADATA") as ArrayList<W>
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

        for (m in data2) {
            if (m.country == "Hàn Quốc") m.country = "Korea"
            if (m.country == "Nga") m.country = "Russia"
            if (m.country == "Nhật Bản") m.country = "Japan"
            if (m.country == "Pháp") m.country = "France"
            if (m.country == "Phần Lan") m.country = "Finland"
            if (m.country == "Thái Lan") m.country = "Thailand"
            if (m.country == "Thụy Điển") m.country = "Sweeden"
            if (m.country == "Trung Quốc") m.country = "China"
            if (m.country == "Tây Ban Nha") m.country = "Spain"
            if (m.country == "Việt Nam") m.country = "Vietnam"
            if (m.country == "Đài Loan") m.country = "Taiwan"
            if (m.country == "Đức") m.country = "Germany"
            if (m.country == "Ấn Độ") m.country = "India"
            if (m.country == "Campuchia") m.country = "Cambodia"

            if (m.cases == "") m.cases = "0"
            if (m.recovered == "") m.recovered = "0"
            if (m.deaths == "") m.deaths = "0"
        }

        var index = 1
        adapter.clear()
        for (m in data2) {
            adapter.add(ItemW(m, index))
            index++
        }
        adapter.notifyDataSetChanged()
        w_list.adapter = adapter

        val dataset = LinkedList(
            listOf(
                "Ca nhiễm giảm dần",
                "Ca nhiễm tăng dần",
                "Ca tử vong giảm dần",
                "Ca tử vong tăng dần",
                "Tên quốc gia A-Z",
                "Tên quốc gia Z-A"
            )
        )
        val adapterSpinner = ArrayAdapter(this, R.layout.spinner_item2, dataset)
        adapterSpinner.setDropDownViewResource(R.layout.spinner_item_chosen)
        wspin.adapter = adapterSpinner
        wspin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                ws.setQuery("", false)
                when (p2) {
                    0 -> {
                        try {
                            var i = 1
                            adapter.clear()
                            for (m in data2) {
                                adapter.add(ItemW(m, i))
                                i++
                            }
                            adapter.notifyDataSetChanged()
                            w_list.smoothScrollToPosition(0)
                        } catch (e: Exception) {
                            Toast.makeText(this@WorldActivity, "Đã xảy ra lỗi", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    1 -> {
                        try {
                            val listCustom = ArrayList<W>()
                            val size = data2.size
                            for (m in size - 1 downTo 0) {
                                listCustom.add(data2[m])
                            }
                            var i = 1
                            adapter.clear()
                            for (m in listCustom) {
                                adapter.add(ItemW(m, i))
                                i++
                            }
                            adapter.notifyDataSetChanged()
                            w_list.smoothScrollToPosition(0)
                        } catch (e: Exception) {
                            Toast.makeText(this@WorldActivity, "Đã xảy ra lỗi", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    4 -> {
                        try {
                            val listCustom = ArrayList<W>()
                            listCustom.addAll(data2)
                            listCustom.sortBy { it.country }
                            var i = 1
                            adapter.clear()
                            for (m in listCustom) {
                                adapter.add(ItemW(m, i))
                                i++
                            }
                            adapter.notifyDataSetChanged()
                            w_list.smoothScrollToPosition(0)
                        } catch (e: Exception) {
                            Toast.makeText(this@WorldActivity, "Đã xảy ra lỗi", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    5 -> {
                        try {
                            val listCustom = ArrayList<W>()
                            listCustom.addAll(data2)
                            listCustom.sortByDescending { it.country }
                            var i = 1
                            adapter.clear()
                            for (m in listCustom) {
                                adapter.add(ItemW(m, i))
                                i++
                            }
                            adapter.notifyDataSetChanged()
                            w_list.smoothScrollToPosition(0)
                        } catch (e: Exception) {
                            Toast.makeText(this@WorldActivity, "Đã xảy ra lỗi", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    2 -> {
                        try {
                            val listCustom = ArrayList<W>()
                            listCustom.addAll(data2)
                            listCustom.sortByDescending { it.deaths.toInt() }
                            var i = 1
                            adapter.clear()
                            for (m in listCustom) {
                                adapter.add(ItemW(m, i))
                                i++
                            }
                            adapter.notifyDataSetChanged()
                            w_list.smoothScrollToPosition(0)
                        } catch (e: Exception) {
                            Toast.makeText(this@WorldActivity, "Đã xảy ra lỗi", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    3 -> {
                        try {
                            val listCustom = ArrayList<W>()
                            listCustom.addAll(data2)
                            listCustom.sortBy { it.deaths.toInt() }
                            var i = 1
                            adapter.clear()
                            for (m in listCustom) {
                                adapter.add(ItemW(m, i))
                                i++
                            }
                            adapter.notifyDataSetChanged()
                            w_list.smoothScrollToPosition(0)
                        } catch (e: Exception) {
                            Toast.makeText(this@WorldActivity, "Đã xảy ra lỗi", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }

        ws.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                try {
                    val dataSearch = ArrayList<W>()
                    if (p0 != null && p0 != "") {
                        for (m in data2) {
                            if (m.country.startsWith(p0, true)) dataSearch.add(m)
                        }
                        var i = 1
                        adapter.clear()
                        for (m in dataSearch) {
                            adapter.add(ItemW(m, i))
                            i++
                        }
                        adapter.notifyDataSetChanged()
                        w_list.smoothScrollToPosition(0)
                    } else {
                        var i = 1
                        adapter.clear()
                        for (m in data2) {
                            adapter.add(ItemW(m, i))
                            i++
                        }
                        adapter.notifyDataSetChanged()
                        w_list.smoothScrollToPosition(0)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@WorldActivity, "Đã xảy ra lỗi", Toast.LENGTH_LONG)
                        .show()
                }
                return true
            }
        })
    }
}

class ItemW(private val w: W, private val i: Int) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item2
    }

    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.w_name.text = "$i.  ${w.country}"

        try {
            if (isNumeric(w.cases)) {
                viewHolder.itemView.ip_case.text =
                    NumberFormat.getNumberInstance(Locale.US).format(w.cases.toInt())
            } else {
                viewHolder.itemView.ip_case.text = w.cases
            }
            if (isNumeric(w.recovered)) {
                viewHolder.itemView.ip_recover.text =
                    NumberFormat.getNumberInstance(Locale.US).format(w.recovered.toInt())
            } else {
                viewHolder.itemView.ip_recover.text = w.recovered
            }
            if (isNumeric(w.deaths)) {
                viewHolder.itemView.ip_death.text =
                    NumberFormat.getNumberInstance(Locale.US).format(w.deaths.toInt())
            } else {
                viewHolder.itemView.ip_death.text = w.deaths
            }

            if (w.new_today == "") {
                viewHolder.itemView.ip_case_new.text = "+0"
            } else {
                viewHolder.itemView.ip_case_new.text = w.new_today
            }

            if (w.today_deaths == "") {
                viewHolder.itemView.ip_death_new.text = "+0"
            } else {
                viewHolder.itemView.ip_death_new.text = w.today_deaths
            }
        } catch (e: Exception) {
            Log.d("error", e.toString())
        }
    }

    private fun isNumeric(s: String): Boolean {
        return try {
            val num = parseInt(s)
            true
        } catch (e: Exception) {
            false
        }
    }
}

class ItemContinent(private val w: W) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item5
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.con_name.text = w.country_vn

        try {
            viewHolder.itemView.con_case.text =
                NumberFormat.getNumberInstance(Locale.US).format(w.cases.toInt())
            viewHolder.itemView.con_recover.text =
                NumberFormat.getNumberInstance(Locale.US).format(w.recovered.toInt())
            viewHolder.itemView.con_death.text =
                NumberFormat.getNumberInstance(Locale.US).format(w.deaths.toInt())
        } catch (e: Exception) {
            Log.d("error", e.toString())
        }
    }
}