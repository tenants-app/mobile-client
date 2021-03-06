package com.tenants.tenants.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tenants.tenants.MainActivity

import com.tenants.tenants.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        activity!!.nav_view.setCheckedItem(R.id.sidebar_home)


        view.homeDebtsCard.setOnClickListener {
            (activity as MainActivity).switchFragment(DebtsFragment.newInstance())
        }

        view.homeTenantsCard.setOnClickListener {
            (activity as MainActivity).switchFragment(UsersFragment.newInstance())
        }

        view.homeBillsCard.setOnClickListener {
            (activity as MainActivity).switchFragment(BillsFragment.newInstance())
        }

        view.homeShoppingCard.setOnClickListener {
            (activity as MainActivity).switchFragment(ShoppingListFragment.newInstance())
        }

        view.homeCleaningCard.setOnClickListener {
            (activity as MainActivity).switchFragment(CleaningFragment.newInstance())
        }

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
        fun newInstance() = HomeFragment()
    }
}
