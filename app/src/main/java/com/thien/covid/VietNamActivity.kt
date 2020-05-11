package com.thien.covid

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
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

class VietNamActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()
    val adapter2 = GroupAdapter<ViewHolder>()
    val listA = ArrayList<Patient>()
    val listB = ArrayList<Patient>()
    val listC = ArrayList<Patient>()
    var isList1ON = true
    var isList2ON = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viet_nam)

        val a = AnimationUtils.loadAnimation(this, R.anim.bounce_in)
        vn_title.startAnimation(a)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        vn_list.layoutManager = layoutManager
        val layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        vn_list2.layoutManager = layoutManager2

        rad.setOnCheckedChangeListener { _, _ ->
            if (rad1.isChecked) {
                adapter2.clear()
                for (m in listA) {
                    adapter2.add(ItemPatient(m))
                }
                adapter2.notifyDataSetChanged()
            } else if (rad2.isChecked) {
                adapter2.clear()
                for (m in listC) {
                    adapter2.add(ItemPatient(m))
                }
                adapter2.notifyDataSetChanged()
            } else if (rad3.isChecked) {
                adapter2.clear()
                for (m in listB) {
                    adapter2.add(ItemPatient(m))
                }
                adapter2.notifyDataSetChanged()
            }
        }

        if (isList1ON) {
            vn_list.visibility = VISIBLE
            vn_text1.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_drop_up,
                0
            )
        } else {
            vn_list.visibility = GONE
            vn_text1.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_drop_down,
                0
            )
        }

        if (isList2ON) {
            vn_list2_all.visibility = VISIBLE
            vn_text2.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_drop_up,
                0
            )
        } else {
            vn_list2_all.visibility = GONE
            vn_text2.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_drop_down,
                0
            )
        }

        vn_text1.setOnClickListener {
            if (!isList1ON) {
                vn_list.visibility = VISIBLE
                isList1ON = true
                vn_text1.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_drop_up,
                    0
                )
            } else {
                vn_list.visibility = GONE
                isList1ON = false
                vn_text1.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_drop_down,
                    0
                )
            }
        }

        vn_text2.setOnClickListener {
            if (!isList2ON) {
                vn_list2_all.visibility = VISIBLE
                isList2ON = true
                vn_text2.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_drop_up,
                    0
                )
            } else {
                vn_list2_all.visibility = GONE
                isList2ON = false
                vn_text2.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_drop_down,
                    0
                )
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
                    listA.add(pat)
                    if (pat.status.contains("Khỏi")) {
                        listB.add(pat)
                    } else {
                        listC.add(pat)
                    }
                }
                for (m in listA) {
                    adapter2.add(ItemPatient(m))
                }
                adapter2.notifyDataSetChanged()
                vn_list2.adapter = adapter2
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
            viewHolder.itemView.ipa_text.text = "${p.id}: Đã khỏi bệnh".replace("BN", "BN ")
            viewHolder.itemView.ipa_text.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            viewHolder.itemView.ipa_text.text = "${p.id}: Đang điều trị".replace("BN", "BN ")
            viewHolder.itemView.ipa_text.setTextColor(Color.parseColor("#ffff4444"))
        }
        viewHolder.itemView.ipa_text2.text =
            "${p.sex}, ${p.age} tuổi, quốc tịch ${p.nationality}, điều trị ở ${p.place}"
    }
}