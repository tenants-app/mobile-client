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
import com.tenants.tenants.models.Debt

class DeptRecyclerViewAdapter(private var context: Context, private var dataList: ArrayList<Debt>, private val onClickListener: (Debt) -> Unit): RecyclerView.Adapter<DeptRecyclerViewAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.debt_item, parent, false))
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.debtName.text = dataList[position].name
        holder.debtDate.text = dataList[position].createdAt.subSequence(0, 10)
        holder.debtValue.text = dataList[position].value.toString() + " zł"
        holder.debtHolder.text = dataList[position].holder.username
        holder.debtBankNumber.text = dataList[position].holder.bank_account_number

        holder.setAsPaid.setOnClickListener { view ->
            onClickListener.invoke(dataList[position])
        }

        if (dataList[position].paid) {
            holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.light_green))
            holder.setAsPaid.visibility = View.GONE
        } else {
            holder.card.setBackgroundColor(Color.WHITE)
            holder.setAsPaid.visibility = View.VISIBLE
        }

    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var debtName: TextView = itemView!!.findViewById(R.id.bill_name)
        var debtDate: TextView = itemView!!.findViewById(R.id.bill_date)
        var debtValue: TextView = itemView!!.findViewById(R.id.debt_value)
        var debtHolder: TextView = itemView!!.findViewById(R.id.bill_holder)
        var debtBankNumber: TextView = itemView!!.findViewById(R.id.bill_holder_bank_number)
        var setAsPaid: Button = itemView!!.findViewById(R.id.set_as_paid_button)
        var card: MaterialCardView = itemView!!.findViewById(R.id.materialCardView)
    }
}