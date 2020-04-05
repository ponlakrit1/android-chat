//package com.example.demochatapp
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.chat_item.view.*
//import java.lang.reflect.Member
//
//class RecyclerAdapterprivate(val items: ArrayList<String>, private val listener: MemberListener): RecyclerView.Adapter<RecyclerAdapterprivate.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.chat_item,
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun getItemCount() = items.size
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(items[position])
//        holder.itemView.setOnClickListener { listener.onItemClick() }
//    }
//
//    class ViewHolder(itemsView: View) : RecyclerView.ViewHolder(itemsView) {
//        fun bind(member: String) {
//            itemView.apply {
////                when (member.generation == 1) {
////                    true -> setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.color_content_1))
////                    false -> setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.color_content_2))
////                }
//
////                textMemberNickName.text = member.nickname.en
////                val fullname = member.firstName.en + " " + member.lastName.en
////                textMemberName.text = fullname
//            }
//        }
//    }
//}
//
//class MemberListener {
//
//}
