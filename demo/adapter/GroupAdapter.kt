package com.nurbk.ps.demochat.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.demochat.R
import com.nurbk.ps.demochat.demo.model.Groups
import kotlinx.android.synthetic.main.item_user_22.view.*

class GroupAdapter(val data: ArrayList<Groups>, val onClick: OnClickItem) :
    RecyclerView.Adapter<GroupAdapter.MyViewHolder>() {

    inner class MyViewHolder(item: View) : RecyclerView.ViewHolder(item)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_user_22, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = data[position]

        holder.itemView.apply {
            username.text = user.name
            txtId.text = user.id
            setOnClickListener {
                onClick.onClick(user)
            }
        }

    }

    override fun getItemCount() = data.size


    interface OnClickItem {
        fun onClick(group: Groups)
    }
}