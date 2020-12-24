package com.nurbk.ps.demochat.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.demochat.R
import com.nurbk.ps.demochat.demo.model.Message
import com.nurbk.ps.demochat.demo.ui.SignIn_SignUpActivity
import kotlinx.android.synthetic.main.item_sende_22.view.*

class MessageAdapter(val dataMessages: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class SenderViewHolder(item: View) : RecyclerView.ViewHolder(item.rootView) {

        fun bind(message: Message) {
            itemView.rootView.txtMes.text = message.message
        }
    }

    inner class ReceverViewHolder(item: View) : RecyclerView.ViewHolder(item.rootView) {
        fun bind(message: Message) {
            itemView.rootView.txtMes.text = message.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            0 -> {
                SenderViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_sende_22, parent, false)
                )
            }
            else -> {
                ReceverViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_recver_22, parent, false)
                )
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is SenderViewHolder) {
            holder.bind(dataMessages[position])
        } else if (holder is ReceverViewHolder) {
            holder.bind(dataMessages[position])
        }

    }

    override fun getItemCount(): Int = dataMessages.size


    override fun getItemViewType(position: Int): Int {
        val message = dataMessages[position]
        return when (message.id) {
            SignIn_SignUpActivity.users.id -> {
                0
            }
            else -> {
                1
            }
        }
    }

}