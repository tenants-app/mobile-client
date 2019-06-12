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
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import com.tenants.tenants.DeptRecyclerViewAdapter

import com.tenants.tenants.R
import com.tenants.tenants.api.DebtsResponse
import com.tenants.tenants.api.RetrofitClient
import com.tenants.tenants.models.*
import com.tenants.tenants.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_debts.view.*
import kotlinx.android.synthetic.main.new_debt_dialog.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList


class DebtsFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var currentGroupId: String? = null
    private var currentGroupMembers: String? = null
    private var dataList: ArrayList<Debt> = ArrayList()
    private lateinit var baseContext: Context
    private lateinit var adapterDebt: DeptRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_debts, container, false)

        activity!!.nav_view.setCheckedItem(R.id.sidebar_debts)


        val sharedPreferences: SharedPrefManager = SharedPrefManager.getInstance(baseContext)
        currentGroupId = sharedPreferences.groupId
        currentGroupMembers = sharedPreferences.groupMembers

        view.fab.setOnClickListener { view ->
            showDialog()
        }

        recyclerView = view.findViewById(R.id.recycler_view_debts)
        adapterDebt = DeptRecyclerViewAdapter(baseContext, dataList, onClickListener = { debt -> onSetDebtAsPaid(debt)})
        recyclerView.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = adapterDebt

        getUserDebts()

        return view
    }


    private fun getUserDebts() {
        RetrofitClient(baseContext).instance.getUserDebts(currentGroupId)
            .enqueue(object : Callback<DebtsResponse> {
                override fun onFailure(call: Call<DebtsResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<DebtsResponse>, response: Response<DebtsResponse>) {
                    if (response.code() == 200) {
                        val debtsCollection: Array<Debt> = response.body()!!.debts
                        debtsCollection.sortBy { debt -> debt.paid }

                        for (debt: Debt in debtsCollection) {
                                dataList.add(debt)
                        }
                    }
                    adapterDebt.notifyDataSetChanged()
                }
            })
    }


    private fun onSetDebtAsPaid(debt: Debt) {
        RetrofitClient(baseContext).instance.setDebtAsPaid(currentGroupId, debt._id)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() == 200) {
                        Toast.makeText(baseContext, getString(R.string.debt_paid), Toast.LENGTH_LONG).show()
                        dataList.get(dataList.indexOf(debt)).paid = true
                        adapterDebt.notifyDataSetChanged()
                    }
                }
            })
    }


    fun showDialog() {
        val mDialogView = LayoutInflater.from(baseContext).inflate(R.layout.new_debt_dialog, null)

        val membersList: Array<Member> = Gson().fromJson(currentGroupMembers, Array<Member>::class.java)
        val membersNamesList: List<String> = membersList.map { member -> member.username }

        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(baseContext, android.R.layout.simple_spinner_item, membersNamesList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDialogView.spinner.setAdapter(spinnerAdapter)
        spinnerAdapter.notifyDataSetChanged()


        val mBuilder = AlertDialog.Builder(baseContext)
            .setView(mDialogView)
            .setTitle("Nowy dług")

        val  mAlertDialog = mBuilder.show()

        mDialogView.dialogAddDebtButton.setOnClickListener {

            val debtName = mDialogView.debtName.text.toString()
            val debtValue = mDialogView.debtValue.text.toString()
            val debtHolder = membersList.get(mDialogView.spinner.selectedItemPosition)._id

            if (debtName.isEmpty()) {
                mDialogView.debtName.error = getString(R.string.debt_name_required)
                mDialogView.debtName.requestFocus()
                return@setOnClickListener
            }

            if (debtValue.isEmpty()) {
                mDialogView.debtValue.error = getString(R.string.debt_value_required)
                mDialogView.debtValue.requestFocus()
                return@setOnClickListener
            }

            mAlertDialog.dismiss()
            addNewDebt(debtName, debtValue.toInt(), debtHolder)
        }

        mDialogView.dialogDebtCancelButton.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }


    fun addNewDebt(debtName: String, debtValue: Int, debtHolder: String) {
        RetrofitClient(baseContext).instance.addNewDebt(currentGroupId, debtName, debtValue, debtHolder)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() == 200) {
                        Toast.makeText(baseContext, getString(R.string.debt_added), Toast.LENGTH_LONG).show()
                        dataList.clear()
                        getUserDebts()
                    }
                }
            })
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseContext = context
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    override fun onDestroyView() {
        super.onDestroyView()
        dataList.clear()
        adapterDebt.notifyDataSetChanged()
    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }


    companion object {
        @JvmStatic
        fun newInstance() = DebtsFragment()
    }
}
