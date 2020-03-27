package com.thien.covid

import android.os.Bundle
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main3.*

class Main3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        supportActionBar?.title = "Thế giới"

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("link_tg")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val link = p0.value.toString()
                Picasso.get()
                    .load(link)
                    .into(picture_frame3, object : Callback {
                        override fun onSuccess() {
                            load_progress3.visibility = GONE
                        }

                        override fun onError(e: Exception?) {
                        }
                    })
            }
        })
    }
}