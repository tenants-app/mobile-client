package com.tenants.tenants.fragments

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
import com.tenants.tenants.DutiesRecyclerViewAdapter

import com.tenants.tenants.R
import com.tenants.tenants.api.RetrofitClient
import com.tenants.tenants.models.DutiesResponse
import com.tenants.tenants.models.Duty
import com.tenants.tenants.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CleaningFragment : Fragment() {
    private var listener: CleaningFragment.OnFragmentInteractionListener? = null
    private var currentGroupId: String? = null
    private var dataList: ArrayList<Duty> = ArrayList()
    private lateinit var baseContext: Context
    private lateinit var adapterDuty: DutiesRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_cleaning, container, false)

        val sharedPreferences: SharedPrefManager = SharedPrefManager.getInstance(baseContext)
        currentGroupId = sharedPreferences.groupId

        recyclerView = view.findViewById(R.id.recycler_view_duties)
        adapterDuty = DutiesRecyclerViewAdapter(baseContext, dataList)
        recyclerView.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = adapterDuty

        getDuties()

        return view
    }


    private fun getDuties() {
        //TODO remove below code
        val duty1 = Duty("", "Filip", "09.06.2019")
        val duty2 = Duty("", "Janusz", "10.06.2019")
        val duty3 = Duty("", "Grazyna", "11.06.2019")

        dataList.add(duty1)
        dataList.add(duty2)
        dataList.add(duty3)

        adapterDuty.notifyDataSetChanged()
//        RetrofitClient(baseContext).instance.getDuties(currentGroupId)
//            .enqueue(object : Callback<DutiesResponse> {
//                override fun onFailure(call: Call<DutiesResponse>, t: Throwable) {
//                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
//                }
//
//                override fun onResponse(call: Call<DutiesResponse>, response: Response<DutiesResponse>) {
//                    if (response.code() == 200) {
//                        for (duty: Duty in response.body()!!.duties) {
//                            dataList.add(duty)
//                        }
//                    }
//                    adapterDuty.notifyDataSetChanged()
//                }
//            })
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseContext = context
        if (context is CleaningFragment.OnFragmentInteractionListener) {
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
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() = CleaningFragment()
    }
}
