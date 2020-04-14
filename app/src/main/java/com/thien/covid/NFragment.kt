package com.thien.covid

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.dialog2.view.*
import kotlinx.android.synthetic.main.fragment_n.*
import kotlinx.android.synthetic.main.item4.view.*
import org.jsoup.Jsoup


class NFragment : Fragment() {

    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_n, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        try {
            GetNews().execute()
        } catch (e: Exception) {
            Toast.makeText(context, "Đã xảy ra lỗi", Toast.LENGTH_LONG).show()
        }

        view.findViewById<RecyclerView>(R.id.n_list).addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        adapter.setOnItemClickListener { item, _ ->
            val lay = layoutInflater.inflate(R.layout.dialog2, null)

            val dialog = AlertDialog.Builder(context)
                .setView(lay)
                .setCancelable(true)
                .show()

            lay.idialog_btn_no.setOnClickListener {
                dialog.dismiss()
            }

            lay.idialog_btn_yes.setOnClickListener {
                dialog.dismiss()
                try {
                    val myItem = item as ItemNews
                    val uri = Uri.parse(myItem.news.link)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "Đã xảy ra lỗi", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    inner class GetNews : AsyncTask<Void, Void, ArrayList<News>>() {
        override fun doInBackground(vararg p0: Void?): ArrayList<News> {
            val list = ArrayList<News>()
            val doc = Jsoup.connect("https://vnexpress.net/covid-19/tin-tuc").get()

            val elementsTop = doc.select("div.wrapper-topstory-folder-v2 article")
            for (e in elementsTop) {
                val e1 = e.select("h1 a").text() //title
                val e2 = e.select("p.description a").text() //content
                val e3 = e.select("h1 a").attr("href") //link
                val e4 = e.select("p.meta-news span.time-public").text() //time
                if (!e1.isNullOrBlank()) list.add(News(e1, e2, e3, e4))
            }

            val elementsMid = doc.select("div.wrapper-topstory-folder-v2 ul.list-sub-feature li")
            for (e in elementsMid) {
                val e1 = e.select("h2 a").text() //title
                val e2 = e.select("p.description a").text() //content
                val e3 = e.select("h2 a").attr("href") //link
                val e4 = e.select("p.meta-news span.time-public").text() //time
                if (!e1.isNullOrBlank()) list.add(News(e1, e2, e3, e4))
            }

            val elementsBot = doc.select("article")
            for (e in elementsBot) {
                val e1 = e.select("h4 a").text() //title
                val e2 = e.select("p.description a").text() //content
                val e3 = e.select("h4 a").attr("href") //link
                val e4 = e.select("p.meta-news span.time-public").text() //time
                if (!e1.isNullOrBlank()) list.add(News(e1, e2, e3, e4))
            }
            return list
        }

        override fun onPostExecute(result: ArrayList<News>?) {
            adapter.clear()
            for (e in result!!) {
                adapter.add(ItemNews(e))
            }
            n_list.adapter = adapter
            super.onPostExecute(result)
        }
    }
}

class News(
    val title: String,
    val content: String,
    val link: String,
    val time: String
)

class ItemNews(val news: News) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item4
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.inews_title.text = news.title
        if (news.content.isEmpty() || news.content.isBlank()) {
            viewHolder.itemView.inews_content.visibility = GONE
        } else {
            viewHolder.itemView.inews_content.text = news.content
        }
        if (news.time.isEmpty() || news.time.isBlank()) {
            viewHolder.itemView.inews_info.visibility = GONE
        } else {
            viewHolder.itemView.inews_info.text = news.time
        }
    }
}