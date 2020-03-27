package com.thien.covid

import android.os.Bundle
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main4.*
import kotlinx.android.synthetic.main.item_row.view.*

class Main4Activity : AppCompatActivity() {

    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        supportActionBar?.title = "Chi tiết"

        list_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("benhnhan")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                adapter.clear()
                val data = p0.value.toString()
                val list = data.split("*")
                for (m in 1 until list.size) {
                    adapter.add(ItemBN(list[m]))
                }
                list_list.adapter = adapter

                runOnUiThread {
                    load_progress4.visibility = GONE
                }
            }
        })
    }
}

class ItemBN(private val s: String) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.item_text.text = "•$s"
    }
}