package com.nurbk.ps.demochat.demo.ui

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nurbk.ps.demochat.R
import com.nurbk.ps.demochat.demo.SocketCreate
import com.nurbk.ps.demochat.demo.adapter.GroupAdapter
import com.nurbk.ps.demochat.demo.adapter.UserAdapter
import com.nurbk.ps.demochat.demo.model.Groups
import com.nurbk.ps.demochat.demo.model.Users
import com.nurbk.ps.demochat.ui.dialog.AddGroupDialogFragment
import kotlinx.android.synthetic.main.activity_list_group.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList


class GroupListActivty : AppCompatActivity() {

    lateinit var app: SocketCreate
    private var mSocket: Socket? = null
    val usersId = ArrayList<String>()
    private val adapterUser = UserAdapter(arrayListOf(), object : UserAdapter.OnClickItem {
        override fun onClick(user: Users) {
            if (!usersId.contains(user.id))
                usersId.add(user.id)
        }
    });
    private val adapterGroup = GroupAdapter(arrayListOf(), object : GroupAdapter.OnClickItem {
        override fun onClick(group: Groups) {
            startActivity(Intent(this@GroupListActivty, MessageGroupActivity::class.java).apply {
                putExtra("group", group)
            })
        }
    });


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_group)
        app = application as SocketCreate
        mSocket = app.getSocket();

        usersId.add(SignIn_SignUpActivity.users.id)

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
        mSocket!!.emit("allGroup", true)
        mSocket!!.on("allGroup") { args ->
            runOnUiThread {
                val groupList: Type = object : TypeToken<List<Groups>>() {}.type
                val groups = Gson().fromJson<List<Groups>>(args[0].toString(), groupList)
                adapterGroup.data.clear()
                groups.forEach { group ->
                    group.usersId.map { id ->
                        if (SignIn_SignUpActivity.users.id == id)
                            adapterGroup.apply {
                                data.add(group)
                                notifyDataSetChanged()
                            }
                    }

                }

            }
        }

        rcDataGroup.apply {
            adapter = adapterGroup
            layoutManager = LinearLayoutManager(this@GroupListActivty)
        }

        btnCreateGroup.setOnClickListener {
            val nameGroup = txtNameGroup.text.toString();
            val dataGroup = JSONObject()
            dataGroup.put("name", nameGroup)
            dataGroup.put("id", UUID.randomUUID().toString())
            val userIdJson = JSONArray()
            for (id in usersId) {
                userIdJson.put(id)
            }
            dataGroup.put("usersId", userIdJson)
            mSocket!!.emit("addGroup", dataGroup)
        }

    }


}