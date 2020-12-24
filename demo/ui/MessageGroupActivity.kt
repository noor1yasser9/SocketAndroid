package com.nurbk.ps.demochat.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.nurbk.ps.demochat.R
import com.nurbk.ps.demochat.demo.adapter.MessageAdapter
import com.nurbk.ps.demochat.demo.SocketCreate
import com.nurbk.ps.demochat.demo.model.Groups
import com.nurbk.ps.demochat.demo.model.Message
import kotlinx.android.synthetic.main.activity_chat.*
import org.json.JSONObject
import java.security.acl.Group


class MessageGroupActivity : AppCompatActivity() {

    lateinit var app: SocketCreate
    private var mSocket: Socket? = null
    private val mesAdapter = MessageAdapter(arrayListOf())
    private lateinit var group: Groups


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        app = application as SocketCreate
        mSocket = app.getSocket();

        group = intent.getParcelableExtra("group")!!
        rcDataMes.apply {
            adapter = mesAdapter
            layoutManager = LinearLayoutManager(applicationContext)
        }

        btnSend.setOnClickListener {
            sendMessage()
        }

        mSocket!!.on("messageGroup") { arg ->
            runOnUiThread {
                if (arg[0].toString() == group.id) {
                val message = Gson().fromJson<Message>(arg[1].toString(), Message::class.java)
                mesAdapter.apply {
                    dataMessages.add(message)
                    notifyDataSetChanged()
                }
                }
            }
        }

    }

    private fun sendMessage() {
        val message = JSONObject()
        message.put("username", SignIn_SignUpActivity.users.username)
        message.put("id", SignIn_SignUpActivity.users.id)
        message.put("message", ed_messege.text.toString())
        mSocket!!.emit("messageGroup", group.id, message)
    }
}