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
                    val timetg = result.data.updated_at
                    val s1 = timetg.substring(11, 19)
                    val s2 = timetg.substring(8, 10)
                    val s3 = timetg.substring(5, 7)
                    val s4 = timetg.substring(0, 4)
                    intent.putExtra("time", "Cập nhật: $s1 $s2-$s3-$s4. Nguồn: Worldometers")

                    val vn = result.gdata.total
                    val vnArr = vn.split("\r\n")
                    val vnArrLast = vnArr[vnArr.size - 1]
                    val arr = vnArrLast.split(",")
                    intent.putExtra("vntotal_cases", arr[2])
                    intent.putExtra("vntotal_deaths", arr[3])
                    intent.putExtra("vntotal_recovered", arr[6])
                    intent.putExtra("textToTransfer", result.data.data[0].table_left)

                    startActivity(intent)
                    finish()
                }
            }
        })
    }
}