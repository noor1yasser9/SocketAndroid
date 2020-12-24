package com.nurbk.ps.demochat.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.nurbk.ps.demochat.R
import com.nurbk.ps.demochat.demo.adapter.MessageAdapter
import com.nurbk.ps.demochat.demo.SocketCreate
import com.nurbk.ps.demochat.demo.model.Message
import kotlinx.android.synthetic.main.activity_chat.*
import org.json.JSONObject


class MessageActivity : AppCompatActivity() {

    lateinit var app: SocketCreate
    private var mSocket: Socket? = null
    private val mesAdapter = MessageAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        app = application as SocketCreate
        mSocket = app.getSocket();


        rcDataMes.apply {
            adapter = mesAdapter
            layoutManager = LinearLayoutManager(applicationContext)
        }

        btnSend.setOnClickListener {
            sendMessage()
        }

        mSocket!!.on("message") { arg ->
            runOnUiThread {
                val message = Gson().fromJson<Message>(arg[0].toString(), Message::class.java)
                mesAdapter.apply {
                    dataMessages.add(message)
                    notifyDataSetChanged()
                }
            }
        }

    }

    private fun sendMessage() {
        val message = JSONObject()
        message.put("username", SignIn_SignUpActivity.users.username)
        message.put("id", SignIn_SignUpActivity.users.id)
        message.put("message", ed_messege.text.toString())

        mSocket!!.emit("message", message)
    }
}