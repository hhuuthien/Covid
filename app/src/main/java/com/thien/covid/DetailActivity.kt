package com.thien.covid

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item1.view.*
import org.jsoup.Jsoup

class DetailActivity : AppCompatActivity() {

    private val adapter2 = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        adapter2.clear()

        val data = FirebaseDatabase.getInstance().getReference("benhnhan")
        data.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val listPatient = ArrayList<Patient>()
                val dataHTML = p0.value.toString()
                val document = Jsoup.parse(dataHTML)
                val elements = document.select("tr")
                for (e in elements) {
                    val arr = e.select("td")
                    listPatient.add(
                        Patient(
                            arr[0].text().toString(),
                            arr[1].text().toString(),
                            arr[2].text().toString(),
                            arr[3].text().toString(),
                            arr[4].text().toString(),
                            arr[5].text().toString()
                        )
                    )
                }

                runOnUiThread {
                    for (m in listPatient) {
                        adapter2.add(ItemPatient(m))
                    }
                    de_list.adapter = adapter2
                    de_load.visibility = GONE
                }
            }
        })
    }
}

class Patient(
    val id: String,
    val age: String,
    val sex: String,
    val place: String,
    val status: String,
    val nationality: String
)

class ItemPatient(private val p: Patient) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item1
    }

    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.ipa1.text = p.id
        viewHolder.itemView.ipa2.text = "${p.sex}\n${p.age} tuổi"
        viewHolder.itemView.ipa22.text = p.nationality
        viewHolder.itemView.ipa3.text = p.place
        viewHolder.itemView.ipa4.text = p.status
    }
}