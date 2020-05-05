package com.thien.covid

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_start.*
import okhttp3.*
import java.io.IOException

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_start)

        val url = "https://vnexpress.net/microservice/corona"
        val request = Request.Builder().url(url).build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gSon = GsonBuilder().create()
                val result = gSon.fromJson(body, SData3::class.java)

                runOnUiThread {
                    val w = result.data.data[0].table_world
                    num_4.text = w.total_cases
                    num_5.text = w.total_deaths
                    num_6.text = w.total_recovered
                    val timetg = result.data.updated_at
                    val s1 = timetg.substring(11, 19)
                    val s2 = timetg.substring(8, 10)
                    val s3 = timetg.substring(5, 7)
                    val s4 = timetg.substring(0, 4)
                    s_texttgtime.text =
                        "Cập nhật lần cuối: $s1 ngày $s2-$s3-$s4.\nNguồn: Worldometers."

                    val vn = result.gdata.total
                    val vnArr = vn.split("\r\n")
                    val vnArrLast = vnArr[vnArr.size - 1]
                    val arr = vnArrLast.split(",")
                    val day = arr[0].replace("/", "-")
                    val time = arr[1]
                    num_1.text = arr[2]
                    num_2.text = arr[3]
                    num_3.text = arr[6]
                    s_textvntime.text =
                        "Cập nhật lần cuối: $time ngày $day-2020.\nNguồn: Bộ Y tế."
                }
            }
        })
    }
}

class SWorldData(
    val total_cases: String,
    val total_deaths: String,
    val total_recovered: String
)

class SVNData(
    val total: String
)

class SData1(
    val table_world: SWorldData
)

class SData2(
    val data: ArrayList<SData1>,
    val updated_at: String
)

class SData3(
    val data: SData2,
    val gdata: SVNData
)