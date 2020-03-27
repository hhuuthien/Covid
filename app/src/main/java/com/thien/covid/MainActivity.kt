package com.thien.covid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Covid-19"

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("khoi")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val data = p0.value.toString()
                val listKhoi = data.split("#")
                case_detail2.text =
                    "• Có ${listKhoi[0]} ca phục hồi trong giai đoạn 2, gồm: ${listKhoi[1]}"
            }
        })

        val queue = Volley.newRequestQueue(this)
        val url =
            "https://vi.wikipedia.org/wiki/%C4%90%E1%BA%A1i_d%E1%BB%8Bch_COVID-19_t%E1%BA%A1i_Vi%E1%BB%87t_Nam"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                val document = Jsoup.parse(response)
                val elements = document.select("table.infobox tr td")
                val line7 = elements[7].toString()
                val number = line7.substringAfter("<td>").substringBefore("<br>")
                val time = line7.substringAfter("đến ").substringBefore("</i>")
                val line8 = elements[8].toString()
                val numberBP = line8.substringAfter("<td>").substringBefore("</td>")
                val line9 = elements[9].toString()
                val numberTV = line9.substringAfter("<td>").substringBefore("</td>")
                case_content.text = number
                case_detail.text =
                    "• Trong đó: $numberBP ca đã phục hồi, $numberTV ca tử vong"
                case_detail_time_update.text = "(Cập nhật lúc $time)"
                case_progress.visibility = GONE
            },
            Response.ErrorListener {
                Log.d("covid19", it.toString())
            })
        queue.add(stringRequest)

        btn_detail.setOnClickListener {
            startActivity(Intent(this, Main2Activity::class.java))
        }

        btn_detailtg.setOnClickListener {
            startActivity(Intent(this, Main3Activity::class.java))
        }

        btn_chitiet.setOnClickListener {
            startActivity(Intent(this, Main4Activity::class.java))
        }
    }
}