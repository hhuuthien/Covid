package com.thien.covid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_w.*
import kotlinx.android.synthetic.main.item.view.*
import okhttp3.*
import java.io.IOException

class WFragment : Fragment() {

    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        return inflater.inflate(R.layout.fragment_w, container, false)
    }

    private fun init() {
        adapter.clear()
        val url = "https://gw.vnexpress.net/cr/?name=world_coronavirus"
        val request = Request.Builder().url(url).build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gSon = GsonBuilder().create()
                val result = gSon.fromJson(body, WorldData::class.java)

                val num1 = result.data.data[0].table_world.total_cases
                val num2 = result.data.data[0].table_world.total_recovered
                val num3 = result.data.data[0].table_world.total_deaths

                val num1w = result.data.data[0].table_world.new_cases
                val num2w = result.data.data[0].table_world.new_recovered
                val num3w = result.data.data[0].table_world.new_deaths

                val listCountry: ArrayList<CaseCountry> = result.data.data[0].table_country

                activity?.runOnUiThread {
                    num_1.text = num1
                    num_2.text = num2
                    num_3.text = num3
                    num_1w.text = num1w
                    num_2w.text = num2w
                    num_3w.text = num3w

                    adapter.clear()
                    for (m in listCountry) {
                        if (m.country != "") adapter.add(ItemCountry(m))
                    }
                    w_list.adapter = adapter
                }
            }
        })
    }
}

class WorldData(
    val data: WorldDataChild
)

class WorldDataChild(
    val data: ArrayList<WorldDataChild2>
)

class WorldDataChild2(
    val table_world: WorldInfo,
    val table_country: ArrayList<CaseCountry>
)

class WorldInfo(
    val total_cases: String,
    val total_deaths: String,
    val total_recovered: String,
    val total_country: String,
    val new_cases: String,
    val new_recovered: String,
    val new_deaths: String
)

class CaseCountry(
    val total_cases: String,
    val total_recovered: String,
    val total_deaths: String,
    val country: String
)

class ItemCountry(private val country: CaseCountry) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item2
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.ip_name.text = country.country

        if (country.total_cases == "") {
            viewHolder.itemView.ip_case.text = "0"
        } else {
            viewHolder.itemView.ip_case.text = country.total_cases
        }

        if (country.total_recovered == "") {
            viewHolder.itemView.ip_recover.text = "0"
        } else {
            viewHolder.itemView.ip_recover.text = country.total_recovered
        }

        if (country.total_deaths == "") {
            viewHolder.itemView.ip_death.text = "0"
        } else {
            viewHolder.itemView.ip_death.text = country.total_deaths
        }
    }
}