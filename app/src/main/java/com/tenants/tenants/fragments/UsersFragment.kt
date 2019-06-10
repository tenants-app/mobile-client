package com.tenants.tenants.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

import com.tenants.tenants.R
import kotlinx.android.synthetic.main.fragment_users.*


class UsersFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = nul
    private lateinit var baseContext: Context
    var mobileArray = arrayOf("Android", "IPhone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_users, container, false)

        val adapter: ArrayAdapter = ArrayAdapter<String>(baseContext, R.layout.user_item, mobileArray)

        usersList.setAdapter(adapter);

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
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

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance() = UsersFragment()
    }
}
