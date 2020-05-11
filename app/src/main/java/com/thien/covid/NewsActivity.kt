package com.thien.covid

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.item3.view.*
import org.jsoup.Jsoup

class NewsActivity : AppCompatActivity() {

    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val a = AnimationUtils.loadAnimation(this, R.anim.bounce_in)
        news_title.startAnimation(a)

        try {
            GetNews().execute()
        } catch (e: Exception) {
            Toast.makeText(this, "Đã xảy ra lỗi", Toast.LENGTH_LONG).show()
            finish()
        }

        adapter.setOnItemClickListener { item, _ ->
            try {
                val myItem = item as ItemNews
                val uri = Uri.parse(myItem.news.link)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Đã xảy ra lỗi", Toast.LENGTH_LONG).show()
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
            news_list.adapter = adapter
            news_loading.visibility = GONE
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
        return R.layout.item3
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.inews_title.text = news.title
        if (news.content.isEmpty() || news.content.isBlank()) {
            viewHolder.itemView.inews_content.visibility = View.GONE
        } else {
            viewHolder.itemView.inews_content.text = news.content
        }
        if (news.time.isEmpty() || news.time.isBlank()) {
            viewHolder.itemView.inews_info.visibility = View.GONE
        } else {
            viewHolder.itemView.inews_info.text = news.time
        }
    }
}