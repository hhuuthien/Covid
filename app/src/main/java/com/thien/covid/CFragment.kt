package com.thien.covid

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.dialog2.view.*
import kotlinx.android.synthetic.main.fragment_c.*
import kotlinx.android.synthetic.main.item5.view.*

class CFragment : Fragment() {

    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_c, container, false)
        init(view)
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun init(view: View) {
        adapter.clear()
        val phone1 = view.findViewById<TextView>(R.id.phone1)
        val phone2 = view.findViewById<TextView>(R.id.phone2)

        adapter.setOnItemClickListener { item, _ ->
            val myItem = item as ItemHospital
            val lay = layoutInflater.inflate(R.layout.dialog2, null)
            lay.idialog_text.text = "Gọi đến ${myItem.hos.name}?"

            val dialog = AlertDialog.Builder(context)
                .setView(lay)
                .setCancelable(true)
                .show()

            lay.idialog_btn_no.setOnClickListener {
                dialog.dismiss()
            }

            lay.idialog_btn_yes.setOnClickListener {
                dialog.dismiss()
                val uri = Uri.parse("tel:${myItem.hos.phone}")
                val intent = Intent(Intent.ACTION_DIAL, uri)
                startActivity(intent)
            }
        }

        phone1.setOnClickListener {
            val uri = Uri.parse("tel:19009095")
            val intent = Intent(Intent.ACTION_DIAL, uri)
            startActivity(intent)
        }

        phone2.setOnClickListener {
            val uri = Uri.parse("tel:19003228")
            val intent = Intent(Intent.ACTION_DIAL, uri)
            startActivity(intent)
        }

        val data = FirebaseDatabase.getInstance().getReference("hospital")
        data.addValueEventListener(object : ValueEventListener {

            val list = ArrayList<Hospital>()

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val hos = p0.children
                for (h in hos) {
                    val hospitalString = h.value.toString()
                    val city = hospitalString.substringAfter("city=").substringBefore(",")
                    val name = hospitalString.substringAfter("name=").substringBefore(",")
                    val phone = hospitalString.substringAfter("phone=").substringBefore(",")
                    val hospital = Hospital(city, phone, name)
                    list.add(hospital)
                }

                activity?.runOnUiThread {
                    for (m in list) {
                        adapter.add(ItemHospital(m))
                    }
                    c_list.adapter = adapter
                }
            }
        })
    }
}

class Hospital(
    val city: String,
    val phone: String,
    val name: String
)

class ItemHospital(
    val hos: Hospital
) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item5
    }

    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.ic1.text = "${hos.name} (${hos.city})"
        viewHolder.itemView.ic2.text = hos.phone
    }
}