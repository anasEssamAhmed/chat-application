package com.app.socketchatdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.socketchatdemo.R
import com.app.socketchatdemo.model.Groups
import kotlinx.android.synthetic.main.group_interface.view.*

class adapterGroups(var data: ArrayList<Groups>, var clike: clikeTheButton) :
    RecyclerView.Adapter<adapterGroups.viewHolder>() {
    inner class viewHolder(item: View) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.group_interface, parent, false)
        return viewHolder(layout)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val group = data[position]
        holder.itemView.apply {
            textNameGroup.text = group.name
            setOnClickListener {
                clike.onClikeTheBtn(group)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface clikeTheButton {
        fun onClikeTheBtn(group: Groups) {

        }
    }
}