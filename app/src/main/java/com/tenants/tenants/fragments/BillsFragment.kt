package com.tenants.tenants.fragments

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tenants.tenants.BillRecyclerViewAdapter

import com.tenants.tenants.R
import com.tenants.tenants.api.RetrofitClient
import com.tenants.tenants.models.*
import com.tenants.tenants.storage.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_debts.view.*
import kotlinx.android.synthetic.main.new_bill_dialog.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BillsFragment : Fragment() {
    private var listener: BillsFragment.OnFragmentInteractionListener? = null
    private var currentGroupId: String? = null
    private var currentGroupMembers: String? = null
    private var dataList: ArrayList<Bill> = ArrayList()
    private lateinit var baseContext: Context
    private lateinit var adapterBill: BillRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_bills, container, false)

        val sharedPreferences: SharedPrefManager = SharedPrefManager.getInstance(baseContext)
        currentGroupId = sharedPreferences.groupId
        currentGroupMembers = sharedPreferences.groupMembers

        view.fab.setOnClickListener { view ->
            showDialog()
        }

        recyclerView = view.findViewById(R.id.recycler_view_bills)
        adapterBill = BillRecyclerViewAdapter(baseContext, dataList, onClickListener = { bill -> onSetBillAsPaid(bill)})
        recyclerView.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = adapterBill

        getUserBills()

        return view
    }


    private fun getUserBills() {
        RetrofitClient(baseContext).instance.getUserBills(currentGroupId)
            .enqueue(object : Callback<BillsResponse> {
                override fun onFailure(call: Call<BillsResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<BillsResponse>, response: Response<BillsResponse>) {
                    if (response.code() == 200) {
                        val billsCollection: Array<Bill> = response.body()!!.bills
                        billsCollection.sortBy { bill -> bill.debtors[0].paid }

                        for (bill: Bill in billsCollection) {
                            dataList.add(bill)
                        }
                    }
                    adapterBill.notifyDataSetChanged()
                }
            })
    }


    private fun onSetBillAsPaid(bill: Bill) {
        RetrofitClient(baseContext).instance.setBillAsPaid(currentGroupId, bill._id)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() == 200) {
                        Toast.makeText(baseContext, getString(R.string.bill_paid), Toast.LENGTH_LONG).show()
                        dataList.get(dataList.indexOf(bill)).debtors[0].paid = true
                        adapterBill.notifyDataSetChanged()
                    }
                }
            })
    }


    fun showDialog() {
        val newBillDialogView = LayoutInflater.from(baseContext).inflate(R.layout.new_bill_dialog, null)

        val mBuilder = AlertDialog.Builder(baseContext)
            .setView(newBillDialogView)
            .setTitle("Nowy rachunek")

        val  mAlertDialog = mBuilder.show()

        newBillDialogView.dialogBillAddButton.setOnClickListener {

            val billName = newBillDialogView.billName.text.toString()
            val billValue = newBillDialogView.billValue.text.toString()

            if (billName.isEmpty()) {
                newBillDialogView.billName.error = getString(R.string.debt_name_required)
                newBillDialogView.billName.requestFocus()
                return@setOnClickListener
            }

            if (billValue.isEmpty()) {
                newBillDialogView.billValue.error = getString(R.string.debt_value_required)
                newBillDialogView.billValue.requestFocus()
                return@setOnClickListener
            }

            mAlertDialog.dismiss()
            addNewBill(billName, billValue.toInt())
        }

        newBillDialogView.dialogBillCancelButton.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }


    fun addNewBill(debtName: String, debtValue: Int) {
        RetrofitClient(baseContext).instance.addNewBill(currentGroupId, debtName, debtValue)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() == 200) {
                        Toast.makeText(baseContext, getString(R.string.bill_added), Toast.LENGTH_LONG).show()
                        dataList.clear()
                        getUserBills()
                    }
                }
            })
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseContext = context
        if (context is BillsFragment.OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }


    companion object {
        @JvmStatic
        fun newInstance() = BillsFragment()
    }
}
