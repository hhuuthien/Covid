package com.thien.covid

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_v.*
import kotlinx.android.synthetic.main.item.view.*
import okhttp3.*
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode

class VFragment : Fragment() {

    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_v, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        adapter.clear()
        val url = "https://gw.vnexpress.net/cr/?name=tracker_coronavirus"
        val request = Request.Builder().url(url).build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gSon = GsonBuilder().create()
                val result = gSon.fromJson(body, DataFather::class.java)

                val trackerTotalByDay = result.data.data[0].tracker_total_by_day
                val num1 = trackerTotalByDay.cases
                val num2 = trackerTotalByDay.recovered
                val num3 = trackerTotalByDay.deaths
                val num4 = num1 - num2 - num3

                val size = result.data.data[0].tracker_by_day.size
                val trackerDay = result.data.data[0].tracker_by_day[size - 1]
                val num1m = trackerDay.cases
                val num2m = trackerDay.recovered
                val num3m = trackerDay.deaths

                val listProvince: ArrayList<CaseProvince> = result.data.data[0].tracker_by_province

                activity?.runOnUiThread {
                    num_1.text = num1.toString()
                    num_2.text = num2.toString()
                    num_3.text = num3.toString()
                    num_4.text = num4.toString()

                    num_1m.text = "+$num1m"
                    num_2m.text = "+$num2m"
                    num_3m.text = "+$num3m"
                    num_4m.text = "-$num2m"

                    val percent2 = BigDecimal((num2.toDouble() * 100 / num1)).setScale(
                        1,
                        RoundingMode.HALF_EVEN
                    )
                    val percent3 = BigDecimal((num3.toDouble() * 100 / num1)).setScale(
                        1,
                        RoundingMode.HALF_EVEN
                    )
                    val percent4 = BigDecimal((num4.toDouble() * 100 / num1)).setScale(
                        1,
                        RoundingMode.HALF_EVEN
                    )

                    num_2a.text = "$percent2%"
                    num_3a.text = "$percent3%"
                    num_4a.text = "$percent4%"

                    adapter.clear()
                    var sum = 0
                    var sum2 = 0
                    for (m in listProvince) {
                        adapter.add(ItemProvince(m))
                        sum += m.cases
                        sum2 += m.recovered
                    }
                    v_list.adapter = adapter
                }
            }
        })

        view.findViewById<TextView>(R.id.v_more).setOnClickListener {
            startActivity(Intent(context, DetailActivity::class.java))
        }
    }
}

class TrackerTotalByDay(
    val cases: Int,
    val deaths: Int,
    val recovered: Int
)

class TrackerByDay(
    val cases: Int,
    val deaths: Int,
    val recovered: Int
)

class DataCovidClass(
    val tracker_total_by_day: TrackerTotalByDay,
    val tracker_by_day: ArrayList<TrackerByDay>,
    val tracker_by_province: ArrayList<CaseProvince>
)

class DataSon(
    val data: ArrayList<DataCovidClass>
)

class DataFather(
    val data: DataSon
)

class CaseProvince(
    val cases: Int,
    val deaths: Int,
    val recovered: Int,
    val name: String
)

class ItemProvince(private val province: CaseProvince) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.ip_name.text = province.name
        viewHolder.itemView.ip_case.text = province.cases.toString()
        viewHolder.itemView.ip_recover.text = province.recovered.toString()
        viewHolder.itemView.ip_death.text = province.deaths.toString()
    }
}