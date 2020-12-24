package com.nurbk.ps.demochat.demo

import android.app.Application
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket


class SocketCreate : Application() {

    private var mSocket: Socket? = IO.socket("http://10.0.0.1:8080")


    fun getSocket(): Socket? {
        return mSocket
    }
}