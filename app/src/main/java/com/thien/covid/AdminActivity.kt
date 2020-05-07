package com.thien.covid

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jsoup.Jsoup

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val data = FirebaseDatabase.getInstance().getReference("tinh")
        data.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val list = ArrayList<Province>()
                val document = Jsoup.parse(p0.value.toString())
                val elements = document.select("tr")
                for (e in elements) {
                    val arr = e.select("td")
                    list.add(
                        Province(
                            arr[0].text().toString(),
                            arr[1].text().toString(),
                            arr[3].text().toString(),
                            arr[4].text().toString()
                        )
                    )
                }

                runOnUiThread {
                    //write to Firebase
                    var a = 0
                    for (m in list) {
                        FirebaseDatabase.getInstance().getReference("province").child(a.toString())
                            .setValue(m)
                        a++
                    }

                    run2()
                }
            }

            private fun run2() {
                val data2 = FirebaseDatabase.getInstance().getReference("benhnhan")
                data2.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val listPatient = ArrayList<Patient>()
                        val document = Jsoup.parse(p0.value.toString())
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
                            //write to Firebase
                            var a = 0
                            for (m in listPatient) {
                                FirebaseDatabase.getInstance().getReference("patient")
                                    .child(a.toString())
                                    .setValue(m)
                                a++
                            }
                            Toast.makeText(this@AdminActivity, "OK", Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }
                })
            }
        })
    }
}