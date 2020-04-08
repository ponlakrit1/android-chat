package com.example.demochatapp.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demochatapp.R
import com.example.demochatapp.model.MessageModel
import kotlinx.android.synthetic.main.chat_item.view.*

class RecyclerAdapter(private val items: ArrayList<MessageModel>, private val listener: RecyclerListener): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.chat_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener { listener.onItemClick() }
    }

    class ViewHolder(itemsView: View): RecyclerView.ViewHolder(itemsView) {
        fun bind(message: MessageModel) {
            itemView.apply {
                textMessage.text = message.message

                if(message.gravityStatus){
                    messageLayout.gravity = Gravity.END;
                } else {
                    messageLayout.gravity = Gravity.START;
                }
            }

        }
    }

}