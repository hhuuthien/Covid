package com.thien.covid

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.io.Serializable
import java.util.*

class LoadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_load)

        val url = "https://vnexpress.net/microservice/corona"
        val request = Request.Builder().url(url).build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()?.replace("[],", "")
                val gSon = GsonBuilder().create()
                val result = gSon.fromJson(body, SData3::class.java)

                runOnUiThread {
                    val intent = Intent(this@LoadActivity, StartActivity::class.java)
                    val w = result.data.data[0].table_world
                    intent.putExtra("total_cases", w.total_cases)
                    intent.putExtra("total_deaths", w.total_deaths)
                    intent.putExtra("total_recovered", w.total_recovered)
                    intent.putExtra("new_cases", w.new_cases)
                    intent.putExtra("new_deaths", w.new_deaths)
                    intent.putExtra("textToTransfer", result.data.data[0].table_left)
                    intent.putExtra("updated", result.data.updated_at)

                    val vn = result.gdata.total
                    val vnArr = vn.split("\r\n")
                    val vnArrLast = vnArr[vnArr.size - 1]
                    val arr = vnArrLast.split(",")
                    intent.putExtra("vntotal_cases", arr[2])
                    intent.putExtra("vntotal_deaths", arr[3])
                    intent.putExtra("vntotal_recovered", arr[6])

                    startActivity(intent)
                    finish()
                }
            }
        })
    }
}

class SWorldData(
    val total_cases: String,
    val total_deaths: String,
    val total_recovered: String,
    val new_cases: String,
    val new_deaths: String
)

class SVNData(
    val total: String
)

class SData1(
    val table_world: SWorldData,
    val table_left: ArrayList<W>
)

class SData2(
    val data: ArrayList<SData1>,
    val updated_at: String
)

class SData3(
    val data: SData2,
    val gdata: SVNData
)

class W(
    var cases: String,
    var country_vn: String,
    var country: String,
    var recovered: String,
    var deaths: String,
    val new_today: String,
    val today_deaths: String
) : Serializable