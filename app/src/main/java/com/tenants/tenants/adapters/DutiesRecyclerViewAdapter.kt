package com.tenants.tenants.adapters

import android.content.Context
import android.graphics.Color
import android.support.design.card.MaterialCardView
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tenants.tenants.R
import com.tenants.tenants.models.Duty

class DutiesRecyclerViewAdapter(private var context: Context, private var dataList: ArrayList<Duty>): RecyclerView.Adapter<DutiesRecyclerViewAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.duty_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dutyUserName.text = dataList[position].username
        holder.dutyDate.text = dataList[position].date


        if (position == 0) {
            holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_green))
            holder.dutyUserName.setTextColor(Color.WHITE)
            holder.dutyDate.setTextColor(Color.WHITE)
        } else {
            holder.card.setBackgroundColor(Color.WHITE)
        }
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var dutyUserName: TextView = itemView!!.findViewById(R.id.duty_username)
        var dutyDate: TextView = itemView!!.findViewById(R.id.duty_date)
        var card: MaterialCardView = itemView!!.findViewById(R.id.materialCardViewDuty)
    }
}