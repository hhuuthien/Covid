package com.thien.covid

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.dialog.view.*
import okhttp3.*
import java.io.IOException
import java.io.Serializable

class StartActivity : AppCompatActivity() {

    var textToTransfer = ArrayList<W>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_start)

        val a = AnimationUtils.loadAnimation(this, R.anim.bounce_in)
        s_title.startAnimation(a)

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
                        "Cập nhật: $s1 $s2-$s3-$s4. Nguồn: Worldometers"

                    val vn = result.gdata.total
                    val vnArr = vn.split("\r\n")
                    val vnArrLast = vnArr[vnArr.size - 1]
                    val arr = vnArrLast.split(",")
                    num_1.text = arr[2]
                    num_2.text = arr[3]
                    num_3.text = arr[6]
                    s_textvntime.text = "Nguồn: Bộ Y tế"

                    textToTransfer = result.data.data[0].table_left
                }
            }
        })

        s_textvnmore.setOnClickListener {
            startActivity(Intent(this, VietNamActivity::class.java))
        }

        s_texttgmore.setOnClickListener {
            startActivity(
                Intent(this, WorldActivity::class.java)
                    .putExtra("DATADATA", textToTransfer)
            )
        }

        num_2.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }

        box1.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }

        box2.setOnClickListener {
            startActivity(Intent(this, NewsActivity::class.java))
        }

        box3.setOnClickListener {
            val lay = layoutInflater.inflate(R.layout.dialog, null)

            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setView(lay)
            alertDialogBuilder.setCancelable(true)

            val dialog = alertDialogBuilder.create()
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.show()

            lay.phone1.setOnClickListener {
                val uri = Uri.parse("tel:19009095")
                val intent = Intent(Intent.ACTION_DIAL, uri)
                startActivity(intent)
            }

            lay.phone2.setOnClickListener {
                val uri = Uri.parse("tel:19003228")
                val intent = Intent(Intent.ACTION_DIAL, uri)
                startActivity(intent)
            }
        }
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
    val cases: String,
    var country_vn: String,
    val recovered: String,
    val deaths: String
) : Serializable