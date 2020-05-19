package com.thien.covid

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.dialog.view.*

class StartActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val deviceName = "${Build.MANUFACTURER} ${Build.MODEL}"
        if (!deviceName.contains("SM-G610F", true)) {
            val timeStamp = System.currentTimeMillis().toString()
            val ud = UserDevice(timeStamp, deviceName)
            FirebaseDatabase.getInstance().getReference("tracking")
                .child(timeStamp)
                .setValue(ud)
        }

        val a = AnimationUtils.loadAnimation(this, R.anim.bounce_in)
        s_title.startAnimation(a)

        num_4.text = intent.getStringExtra("total_cases")
        num_5.text = intent.getStringExtra("total_deaths")
        num_6.text = intent.getStringExtra("total_recovered")

        num_1.text = intent.getStringExtra("vntotal_cases")
        num_2.text = intent.getStringExtra("vntotal_deaths")
        num_3.text = intent.getStringExtra("vntotal_recovered")

        num_7.text = intent.getStringExtra("new_cases")
        num_8.text = intent.getStringExtra("new_deaths")
        num_9.text = ""

        try {
            val string = intent.getStringExtra("updated")!!.substring(11, 16)
            txtSrc.append("\nCập nhật lúc $string hôm nay.")
        } catch (e: Exception) {
        }

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

class UserDevice(
    val timeStamp: String,
    val deviceName: String
)