package com.thien.covid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.dialog.view.*
import java.io.Serializable

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val a = AnimationUtils.loadAnimation(this, R.anim.bounce_in)
        s_title.startAnimation(a)

        num_4.text = intent.getStringExtra("total_cases")
        num_5.text = intent.getStringExtra("total_deaths")
        num_6.text = intent.getStringExtra("total_recovered")
        num_1.text = intent.getStringExtra("vntotal_cases")
        num_2.text = intent.getStringExtra("vntotal_deaths")
        num_3.text = intent.getStringExtra("vntotal_recovered")
        s_texttgtime.text = intent.getStringExtra("time")
        s_textvntime.text = "Nguồn: Bộ Y tế"

        s_textvnmore.setOnClickListener {
            startActivity(Intent(this, VietNamActivity::class.java))
        }

        s_texttgmore.setOnClickListener {
            startActivity(
                Intent(this, WorldActivity::class.java)
                    .putExtra("DATADATA", intent.getSerializableExtra("textToTransfer"))
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