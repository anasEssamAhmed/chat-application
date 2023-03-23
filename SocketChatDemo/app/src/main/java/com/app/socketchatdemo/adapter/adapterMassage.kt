package com.app.socketchatdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.socketchatdemo.R
import com.app.socketchatdemo.imageAndDecode.imageDecode
import com.app.socketchatdemo.model.massage
import com.app.socketchatdemo.singIn
import kotlinx.android.synthetic.main.sender_massage.view.*

class adapterMassage(var data: ArrayList<massage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var imageTo = imageDecode()
    inner class SenderViewHolder(item: View) : RecyclerView.ViewHolder(item.rootView) {
        fun sendMassage(massage1: massage) {
            if (massage1.massage.length <= 500) {
                itemView.rootView.text_sender.visibility = View.VISIBLE
                itemView.rootView.text_sender.text = massage1.massage
                itemView.rootView.imageSrc.visibility = View.GONE
            } else {
                itemView.rootView.imageSrc.setImageBitmap(imageTo.decodeImage(massage1.massage))
                itemView.rootView.text_sender.visibility = View.GONE
                itemView.rootView.imageSrc.visibility = View.VISIBLE
            }
        }
    }

    inner class ReceiverViewHolder(item: View) : RecyclerView.ViewHolder(item.rootView) {
        fun receiverMassage(massage1: massage) {
            if (massage1.massage.length <= 200) {
                itemView.rootView.text_sender.visibility = View.VISIBLE
                itemView.rootView.text_sender.text = massage1.massage
                itemView.rootView.imageSrc.visibility = View.GONE
            } else {
                itemView.rootView.imageSrc.setImageBitmap(imageTo.decodeImage(massage1.massage))
                itemView.rootView.text_sender.visibility = View.GONE
                itemView.rootView.imageSrc.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

       return when (viewType) {
            0 -> {
                val layout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.sender_massage, parent, false)
                SenderViewHolder(layout)
            }
            else -> {
                val layout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.reciver_massage, parent, false)
                ReceiverViewHolder(layout)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SenderViewHolder) {
            holder.sendMassage(data[position])
        } else if (holder is ReceiverViewHolder) {
            holder.receiverMassage(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        var massage = data[position]
        return when (massage.id) {
            singIn.users.id -> {
                0
            }
            else -> {
                1
            }
        }
    }

}