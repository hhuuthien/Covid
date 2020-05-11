package com.thien.covid

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.dialog2.view.*
import kotlinx.android.synthetic.main.item4.view.*
import okhttp3.*
import java.io.IOException

class InfoActivity : AppCompatActivity() {

    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val a = AnimationUtils.loadAnimation(this, R.anim.bounce_in)
        info_title.startAnimation(a)

        info_box1.setOnClickListener {
            val appIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:dQHUK2MfXvI"))
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=dQHUK2MfXvI")
            )
            try {
                startActivity(appIntent)
            } catch (ex: ActivityNotFoundException) {
                startActivity(webIntent)
            }
        }

        info_box2.setOnClickListener {
            val appIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:_UEVndsldCM"))
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=_UEVndsldCM")
            )
            try {
                startActivity(appIntent)
            } catch (ex: ActivityNotFoundException) {
                startActivity(webIntent)
            }
        }

        info_box3.setOnClickListener {
            val appIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:k0VMMVkbvNo"))
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=k0VMMVkbvNo")
            )
            try {
                startActivity(appIntent)
            } catch (ex: ActivityNotFoundException) {
                startActivity(webIntent)
            }
        }

        info_box4.setOnClickListener {
            val appIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:LcSbdjWo0rc"))
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=LcSbdjWo0rc")
            )
            try {
                startActivity(appIntent)
            } catch (ex: ActivityNotFoundException) {
                startActivity(webIntent)
            }
        }

        val url = "https://gw.vnexpress.net/cr/?name=answer_question_coronavirus"
        val request = Request.Builder().url(url).build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gSon = GsonBuilder().create()
                val result = gSon.fromJson(body, QuestionFather::class.java)

                val listQ: ArrayList<Question> = result.data.data[0].answer_question
                listQ.shuffle()

                runOnUiThread {
                    adapter.clear()
                    for (m in listQ) {
                        adapter.add(ItemQ(m))
                    }
                    info_list.adapter = adapter
                }
            }
        })

        adapter.setOnItemClickListener { item, _ ->
            val myItem = item as ItemQ
            val layoutInflater = layoutInflater.inflate(R.layout.dialog2, null)
            layoutInflater.ip_question.text = myItem.q.Question

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                layoutInflater.ip_answer.text =
                    Html.fromHtml(myItem.q.Answer, Html.FROM_HTML_MODE_LEGACY)
            } else {
                layoutInflater.ip_answer.text = Html.fromHtml(myItem.q.Answer)
            }

            val builder = AlertDialog.Builder(this)
                .setView(layoutInflater)
                .setCancelable(true)
            val dialog = builder.create()
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.show()
        }
    }
}

class QuestionFather(
    val data: QuestionChild
)

class QuestionChild(
    val data: ArrayList<QuestionChild2>
)

class QuestionChild2(
    val answer_question: ArrayList<Question>
)

class Question(
    val Question: String,
    val Answer: String
)

class ItemQ(val q: Question) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item4
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.in_title.text = q.Question
    }
}