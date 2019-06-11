package com.tenants.tenants

import android.content.Context
import android.graphics.Color
import android.support.design.card.MaterialCardView
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.tenants.tenants.models.ShoppingList

class ShoppingListRecyclerViewAdapter(private var context: Context, private var dataList: ArrayList<ShoppingList>, private val onClickListener: (ShoppingList) -> Unit): RecyclerView.Adapter<ShoppingListRecyclerViewAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.shop_list_item, parent, false))
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.shoppingListName.text = dataList[position].name
        holder.shoppingListDate.text = dataList[position].createdAt.subSequence(0, 10)

        holder.showDetails.setOnClickListener { view ->
            onClickListener.invoke(dataList[position])
        }

        if (dataList[position].debtors[0].paid) {
            holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.light_green))
        } else {
            holder.card.setBackgroundColor(Color.WHITE)
        }
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var shoppingListName: TextView = itemView!!.findViewById(R.id.shopping_list_name)
        var shoppingListDate: TextView = itemView!!.findViewById(R.id.shopping_list_date)
        var showDetails: Button = itemView!!.findViewById(R.id.show_details_button)
        var card: MaterialCardView = itemView!!.findViewById(R.id.materialCardView)
    }
}