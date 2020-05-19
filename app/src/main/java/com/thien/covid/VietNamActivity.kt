package com.thien.covid

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_viet_nam.*
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.item1.view.*
import java.util.*
import kotlin.collections.ArrayList

class VietNamActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()
    val adapter2 = GroupAdapter<ViewHolder>()
    val listAll = ArrayList<Patient>()
    val listDCK = ArrayList<Patient>()
    val listDDT = ArrayList<Patient>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viet_nam)

        val a = AnimationUtils.loadAnimation(this, R.anim.bounce_in)
        vn_title.startAnimation(a)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        vn_list.layoutManager = layoutManager
        val layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        vn_list2.layoutManager = layoutManager2
        vn_list.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        vn_list2.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))

        vn_list.visibility = GONE
        vn_list2.visibility = GONE

        val dataset = LinkedList(listOf("Tất cả", "Đang điều trị", "Đã khỏi bệnh"))
        val adapterSpinner = ArrayAdapter(this, R.layout.spinner_item, dataset)
        adapterSpinner.setDropDownViewResource(R.layout.spinner_item_chosen)
        spin.adapter = adapterSpinner
        spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> {
                        adapter2.clear()
                        for (m in listAll) {
                            adapter2.add(ItemPatient(m))
                        }
                        adapter2.notifyDataSetChanged()
                    }
                    1 -> {
                        adapter2.clear()
                        for (m in listDDT) {
                            adapter2.add(ItemPatient(m))
                        }
                        adapter2.notifyDataSetChanged()
                    }
                    2 -> {
                        adapter2.clear()
                        for (m in listDCK) {
                            adapter2.add(ItemPatient(m))
                        }
                        adapter2.notifyDataSetChanged()
                    }
                }
            }
        }

        val data = FirebaseDatabase.getInstance().getReference("province")
        data.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (m in p0.children) {
                    val proName = m.child("name").value.toString()
                    val proCase = m.child("case").value.toString()
                    val proRecover = m.child("recover").value.toString()
                    val proDeath = m.child("death").value.toString()
                    val pro = Province(proName, proCase, proRecover, proDeath)
                    adapter.add(ItemProvince(pro))
                }
                vn_list.adapter = adapter
                vn_list.visibility = VISIBLE
                loadvn.visibility = GONE
            }
        })

        val data2 = FirebaseDatabase.getInstance().getReference("patient")
        data2.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (m in p0.children) {
                    val patAge = m.child("age").value.toString()
                    val patID = m.child("id").value.toString()
                    val patNationality = m.child("nationality").value.toString()
                    val patPlace = m.child("place").value.toString()
                    val patSex = m.child("sex").value.toString()
                    val patStatus = m.child("status").value.toString()
                    val pat = Patient(patID, patAge, patSex, patPlace, patStatus, patNationality)
                    listAll.add(pat)
                    if (pat.status.contains("Khỏi")) {
                        listDCK.add(pat)
                    } else {
                        listDDT.add(pat)
                    }
                }
                for (m in listAll) {
                    adapter2.add(ItemPatient(m))
                }
                adapter2.notifyDataSetChanged()
                vn_list2.adapter = adapter2
                vn_list2.visibility = VISIBLE
                loadvn2.visibility = GONE
            }
        })
    }
}

class Province(
    val name: String,
    val case: String,
    val recover: String,
    val death: String
)

class ItemProvince(private val p: Province) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.ip_name.text = p.name
        viewHolder.itemView.ip_case.text = p.case
        viewHolder.itemView.ip_recover.text = p.recover
        viewHolder.itemView.ip_death.text = p.death
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
        if (p.status.contains("Khỏi")) {
            viewHolder.itemView.ipa_2.text = "Đã khỏi bệnh"
            viewHolder.itemView.ipa_2.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            viewHolder.itemView.ipa_2.text = "Đang điều trị"
            viewHolder.itemView.ipa_2.setTextColor(Color.parseColor("#ffff4444"))
        }
        viewHolder.itemView.ipa_1.text = "${p.sex}, ${p.age} tuổi"
        viewHolder.itemView.ipa_3.text = "QT: ${p.nationality}"
        viewHolder.itemView.ipa_4.text = "ĐĐ: ${p.place}"
        viewHolder.itemView.lay0.text = p.id.replace("BN", "")
    }
}