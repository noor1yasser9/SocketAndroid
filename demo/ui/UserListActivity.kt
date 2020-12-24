package com.nurbk.ps.demochat.demo.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nurbk.ps.demochat.R
import com.nurbk.ps.demochat.demo.SocketCreate
import com.nurbk.ps.demochat.demo.adapter.UserAdapter
import com.nurbk.ps.demochat.demo.model.Users
import kotlinx.android.synthetic.main.activity_list_user.*
import java.lang.reflect.Type


class UserListActivity : AppCompatActivity() {

    lateinit var app: SocketCreate
    private var mSocket: Socket? = null

    private val adapterUser = UserAdapter(arrayListOf(), object : UserAdapter.OnClickItem {
        override fun onClick(user: Users) {
            startActivity(Intent(applicationContext, MessageActivity::class.java))
        }
    });


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)
        app = application as SocketCreate
        mSocket = app.getSocket();


        rcDataUser.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = adapterUser
        }

        mSocket!!.emit("allUser", true)
        mSocket!!.on("allUser") { ars ->
            runOnUiThread {
                val usersList: Type = object : TypeToken<List<Users>>() {}.type
                val userList = Gson().fromJson<List<Users>>(ars[0].toString(), usersList)
                adapterUser.apply {
                    data.clear()
                    data.addAll(userList)
                    notifyDataSetChanged()
                }
            }
        }

        btnSeeAll.setOnClickListener {
            startActivity(Intent(this, GroupListActivty::class.java))
        }

    }


}