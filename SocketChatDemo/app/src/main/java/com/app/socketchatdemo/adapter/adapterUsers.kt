package com.app.socketchatdemo.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.socketchatdemo.R
import com.app.socketchatdemo.model.data
import kotlinx.android.synthetic.main.view_all_users.view.*

class adapterUsers(var data : ArrayList<data> , var clike : clikeTheButton) : RecyclerView.Adapter<adapterUsers.viewHolder>() {
    inner class viewHolder(item : View) : RecyclerView.ViewHolder(item)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.view_all_users , parent , false)
        return viewHolder(layout)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        var user = data[position]
        holder.itemView.apply {
            textName.text = user.name
            setOnClickListener{
                clike.onClikeTheBtn(user)
            }
            if(user.isTrue) {
                imageOnlineOrNot.setImageResource(R.drawable.online)
            }else {
                imageOnlineOrNot.setImageResource(R.drawable.offline)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface clikeTheButton {
        fun onClikeTheBtn(user : data){

        }
    }

}