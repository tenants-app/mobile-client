package com.tenants.tenants

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tenants.tenants.models.Model

class RecyclerViewAdapter(private var context: Context, private var dataList: ArrayList<Model>, private val onClickListener: (String) -> Unit): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_view, parent, false))
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.textView.text = dataList[position].getDisplayName()
        holder.textView.setOnClickListener { view ->
            onClickListener.invoke(dataList[position].getId())
        }
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var textView: TextView = itemView!!.findViewById(R.id.group_title)
    }
}