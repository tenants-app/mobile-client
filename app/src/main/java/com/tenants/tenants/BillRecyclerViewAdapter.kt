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
import com.tenants.tenants.models.Bill

class BillRecyclerViewAdapter(private var context: Context, private var dataList: ArrayList<Bill>, private val onClickListener: (Bill) -> Unit): RecyclerView.Adapter<BillRecyclerViewAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.bill_item, parent, false))
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.billName.text = dataList[position].name
        holder.billDate.text = dataList[position].createdAt.subSequence(0, 10)
        holder.billValue.text = dataList[position].value.toString() + " zł"
        holder.billValueDivided.text = dataList[position].debtors[0].value.toString() + " zł"
        holder.billHolder.text = dataList[position].user.username
        holder.billHolderBankNumber.text = dataList[position].user.bank_account_number

        holder.setAsPaid.setOnClickListener { view ->
            onClickListener.invoke(dataList[position])
        }

        if (dataList[position].debtors[0].paid) {
            holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.light_green))
            holder.setAsPaid.visibility = View.GONE
        } else {
            holder.card.setBackgroundColor(Color.WHITE)
            holder.setAsPaid.visibility = View.VISIBLE
        }
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var billName: TextView = itemView!!.findViewById(R.id.bill_name)
        var billDate: TextView = itemView!!.findViewById(R.id.bill_date)
        var billValue: TextView = itemView!!.findViewById(R.id.bill_value)
        var billValueDivided: TextView = itemView!!.findViewById(R.id.bill_value_divided)
        var billHolder: TextView = itemView!!.findViewById(R.id.bill_holder)
        var billHolderBankNumber: TextView = itemView!!.findViewById(R.id.bill_holder_bank_number)
        var setAsPaid: Button = itemView!!.findViewById(R.id.set_as_paid_button)
        var card: MaterialCardView = itemView!!.findViewById(R.id.materialCardView)

    }
}