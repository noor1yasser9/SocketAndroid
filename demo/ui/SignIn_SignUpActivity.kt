package com.nurbk.ps.demochat.demo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.nurbk.ps.demochat.R
import com.nurbk.ps.demochat.demo.SocketCreate
import com.nurbk.ps.demochat.demo.model.Users
import kotlinx.android.synthetic.main.activity_auth.*
import org.json.JSONObject
import java.util.*


class SignIn_SignUpActivity : AppCompatActivity() {

    lateinit var app: SocketCreate
    private var mSocket: Socket? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        app = application as SocketCreate
        mSocket = app.getSocket();

        mSocket!!.on(Socket.EVENT_CONNECT_ERROR) {
            runOnUiThread {
                Log.e("EVENT_CONNECT_ERROR", "EVENT_CONNECT_ERROR: ")
            }
        };
        mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, Emitter.Listener {
            runOnUiThread {
                Log.e("EVENT_CONNECT_TIMEOUT", "EVENT_CONNECT_TIMEOUT: ")

            }
        })
        mSocket!!.on(
            Socket.EVENT_CONNECT
        ) { Log.e("onConnect", "Socket Connected!") };
        mSocket!!.on(Socket.EVENT_DISCONNECT, Emitter.Listener {
            runOnUiThread {
                Log.e("onDisconnect", "Socket onDisconnect!")

            }
        })
        mSocket!!.connect()

        val idApp = UUID.randomUUID().toString()
        mSocket!!.on("SingUp") { args ->
            Log.e("ttttttttt", "${args[0]}")
            runOnUiThread {
                if (args[0].toString() == idApp)
                    Toast.makeText(this, "${args[1]}", Toast.LENGTH_LONG).show()
            }
        }




        btnSinUp.setOnClickListener {
            val username = txtName.text.toString()
            val pass1 = txtPassword.text.toString()
            val pass2 = txtPassword2.text.toString()
            val jsonObject = JSONObject()
            jsonObject.put("username", username)
            jsonObject.put("id", UUID.randomUUID().toString())
            jsonObject.put("pass1", pass1)
            jsonObject.put("pass2", pass2)
            mSocket!!.emit("SingUp", idApp, jsonObject)
        }


        mSocket!!.on("SingIn") { args ->
            Log.e("ttttttttt", "${args[0]}")
            runOnUiThread {
                if (args[0].toString() == idApp)
                    if (args[1].toString().toBoolean()) {
                        Toast.makeText(this, "${args[1]}", Toast.LENGTH_LONG).show()
                        users = Gson().fromJson(args[2].toString(), Users::class.java)
                        startActivity(Intent(applicationContext, UserListActivity::class.java))
                    } else {
                        Toast.makeText(this, "${args[0]}", Toast.LENGTH_LONG).show()
                    }
            }
        }

        btnSinIn.setOnClickListener {
            val username = txtUsername.text.toString()
            val pass = txtSinInPassword.text.toString()
            val jsonObject = JSONObject()
            jsonObject.put("user", username)
            jsonObject.put("pass", pass)
            mSocket!!.emit("SingIn", idApp, jsonObject)
        }


    }

    companion object {
        lateinit var users: Users
    }

}