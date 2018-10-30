package com.dung.demoloadmorefirebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), ILoadMore {


    private val TAG = "MainActivity"
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var myAdapter: MyAdapter
    private var users: ArrayList<User?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.reference
        init()
    }

    private fun init() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rcvUser.layoutManager = linearLayoutManager

        myAdapter = MyAdapter(this, rcvUser, users)
        rcvUser.adapter = myAdapter
        Toast.makeText(this, "init", Toast.LENGTH_SHORT).show()
        myAdapter.setLoadMore(this)

        //so luong data duoc load dau tien
        random(20, 0)
        Log.d(TAG, "init....")
    }

    override fun loadMore(visibleThreshold: Int, lastVisibleItem: Int) {
        users.add(null)
        myAdapter.notifyItemInserted(users.size - 1)//bao cho adapter la co su thay doi
        random(20, lastVisibleItem)
        myAdapter.setLoaded()

    }

    private var dataRoot: DataSnapshot? = null
    private fun random(itemTiepTheo: Int, itemDaCo: Int) {

        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                dataRoot = p0
                getData(p0, itemTiepTheo, itemDaCo)
                myAdapter.setLoaded()

            }
        }

        if (dataRoot != null) {
            getData(dataRoot!!, itemTiepTheo, itemDaCo)
        } else {
            reference.child("User").addValueEventListener(valueEventListener)

        }
    }

    fun getData(p0: DataSnapshot, itemTiepTheo: Int, itemDaCo: Int) {
//        users.clear()

        var i = 0//dem  vong for lap dc bao nhieu lan
        for (data: DataSnapshot in p0.children) {
            if (i == itemTiepTheo) {// so luong thang load tiep theo
                break
            }

            if (i < itemDaCo) {// bo qua nhung thang da load roi
                i++
                continue
            }
            i++ //khi lay dc item cung phai tang i len
            val user: User = data.getValue(User::class.java)!!
            users.add(user)
        }
        Toast.makeText(this@MainActivity, "${users.size}", Toast.LENGTH_SHORT).show()
        myAdapter.notifyDataSetChanged()

    }
}
