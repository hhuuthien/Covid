package com.thien.covid

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.dialog.view.*
import kotlinx.android.synthetic.main.fragment_i.*
import kotlinx.android.synthetic.main.item3.view.ip_question
import okhttp3.*
import java.io.IOException

class IFragment : Fragment() {

    private val adapter = GroupAdapter<ViewHolder>()
    private val list1 = ArrayList<Question>()
    private val list2 = ArrayList<Question>()
    private val list3 = ArrayList<Question>()
    private val list4 = ArrayList<Question>()
    private val list5 = ArrayList<Question>()
    private val list6 = ArrayList<Question>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_i, container, false)
        init(v)
        return v
    }

    @SuppressLint("InflateParams")
    private fun init(view: View) {
        adapter.clear()
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

                for (m in listQ) {
                    if (m.Topic == "Triệu chứng, lây nhiễm") list1.add(m)
                    if (m.Topic == "Cách ly") list2.add(m)
                    if (m.Topic == "Điều trị" || m.Topic == "Làm gì khi thấy nguy cơ") list3.add(m)
                    if (m.Topic == "Chính sách phòng, chống dịch" || m.Topic == "Chính sách cho người lao động") list4.add(
                        m
                    )
                    if (m.Topic == "Giao tiếp xã hội mùa dịch" || m.Topic == "Di chuyển mùa dịch") list5.add(
                        m
                    )
                    if (m.Topic == "Vệ sinh phòng dịch") list6.add(m)
                }

                activity?.runOnUiThread {
                    adapter.clear()
                    ilay11.setCardBackgroundColor(Color.parseColor("#4DF44336"))
                    for (m in list1) {
                        adapter.add(ItemQ(m))
                    }
                    i_list.adapter = adapter
                }
            }
        })

        adapter.setOnItemClickListener { item, _ ->
            val myItem = item as ItemQ
            val layoutInflater = layoutInflater.inflate(R.layout.dialog, null)
            layoutInflater.ip_question.text = myItem.q.Question

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                layoutInflater.ip_answer.text =
                    Html.fromHtml(myItem.q.Answer, Html.FROM_HTML_MODE_LEGACY)
            } else {
                layoutInflater.ip_answer.text = Html.fromHtml(myItem.q.Answer)
            }

            val builder = AlertDialog.Builder(context)
                .setView(layoutInflater)
                .setCancelable(true)
            val dialog = builder.create()
            dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.show()
        }

        val a = view.findViewById<CardView>(R.id.ilay11)
        val b = view.findViewById<CardView>(R.id.ilay12)
        val c = view.findViewById<CardView>(R.id.ilay13)
        val d = view.findViewById<CardView>(R.id.ilay21)
        val e = view.findViewById<CardView>(R.id.ilay22)
        val f = view.findViewById<CardView>(R.id.ilay23)
        val list = view.findViewById<RecyclerView>(R.id.i_list)
        val colorActive = Color.parseColor("#4DF44336")
        val colorPassive = Color.parseColor("#1AF44336")

        a.setOnClickListener {
            a.setCardBackgroundColor(colorActive)
            b.setCardBackgroundColor(colorPassive)
            c.setCardBackgroundColor(colorPassive)
            d.setCardBackgroundColor(colorPassive)
            e.setCardBackgroundColor(colorPassive)
            f.setCardBackgroundColor(colorPassive)
            adapter.clear()
            for (m in list1) {
                adapter.add(ItemQ(m))
            }
            list.adapter = adapter
        }

        b.setOnClickListener {
            b.setCardBackgroundColor(colorActive)
            a.setCardBackgroundColor(colorPassive)
            c.setCardBackgroundColor(colorPassive)
            d.setCardBackgroundColor(colorPassive)
            e.setCardBackgroundColor(colorPassive)
            f.setCardBackgroundColor(colorPassive)
            adapter.clear()
            for (m in list2) {
                adapter.add(ItemQ(m))
            }
            list.adapter = adapter
        }

        c.setOnClickListener {
            c.setCardBackgroundColor(colorActive)
            b.setCardBackgroundColor(colorPassive)
            a.setCardBackgroundColor(colorPassive)
            d.setCardBackgroundColor(colorPassive)
            e.setCardBackgroundColor(colorPassive)
            f.setCardBackgroundColor(colorPassive)
            adapter.clear()
            for (m in list3) {
                adapter.add(ItemQ(m))
            }
            list.adapter = adapter
        }

        d.setOnClickListener {
            d.setCardBackgroundColor(colorActive)
            b.setCardBackgroundColor(colorPassive)
            c.setCardBackgroundColor(colorPassive)
            a.setCardBackgroundColor(colorPassive)
            e.setCardBackgroundColor(colorPassive)
            f.setCardBackgroundColor(colorPassive)
            adapter.clear()
            for (m in list4) {
                adapter.add(ItemQ(m))
            }
            list.adapter = adapter
        }

        e.setOnClickListener {
            e.setCardBackgroundColor(colorActive)
            b.setCardBackgroundColor(colorPassive)
            c.setCardBackgroundColor(colorPassive)
            d.setCardBackgroundColor(colorPassive)
            a.setCardBackgroundColor(colorPassive)
            f.setCardBackgroundColor(colorPassive)
            adapter.clear()
            for (m in list5) {
                adapter.add(ItemQ(m))
            }
            list.adapter = adapter
        }

        f.setOnClickListener {
            f.setCardBackgroundColor(colorActive)
            b.setCardBackgroundColor(colorPassive)
            c.setCardBackgroundColor(colorPassive)
            d.setCardBackgroundColor(colorPassive)
            e.setCardBackgroundColor(colorPassive)
            a.setCardBackgroundColor(colorPassive)
            adapter.clear()
            for (m in list6) {
                adapter.add(ItemQ(m))
            }
            list.adapter = adapter
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
    val Answer: String,
    val Topic: String
)

class ItemQ(val q: Question) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item3
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.ip_question.text = q.Question
    }
}