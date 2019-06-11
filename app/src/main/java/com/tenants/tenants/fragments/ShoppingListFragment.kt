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
import com.tenants.tenants.MainActivity

import com.tenants.tenants.R
import com.tenants.tenants.ShoppingListRecyclerViewAdapter
import com.tenants.tenants.models.BillDebtor
import com.tenants.tenants.models.Product
import com.tenants.tenants.models.ShoppingList
import com.tenants.tenants.models.User
import com.tenants.tenants.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_shopping_list.view.*

class ShoppingListFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var currentGroupId: String? = null
    private var currentGroupMembers: String? = null
    private var dataList: ArrayList<ShoppingList> = ArrayList()
    private lateinit var baseContext: Context
    private lateinit var adapterShoppingList: ShoppingListRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_shopping_list, container, false)

        activity!!.nav_view.setCheckedItem(R.id.sidebar_shopping_list)


        val sharedPreferences: SharedPrefManager = SharedPrefManager.getInstance(baseContext)
        currentGroupId = sharedPreferences.groupId
        currentGroupMembers = sharedPreferences.groupMembers

        view.add_shopping_list.setOnClickListener { view ->
            //
        }

        recyclerView = view.findViewById(R.id.recycler_view_shopping_list)
        adapterShoppingList = ShoppingListRecyclerViewAdapter(baseContext, dataList, onClickListener = { shoppingList -> showDetails(shoppingList)})
        recyclerView.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = adapterShoppingList

        getShoppingList()

        return view
    }


    private fun getShoppingList() {
        //TODO remove below code

        val user = User("Filip", "filip@exmple.com", "1243 1234 1234 1234 1234 1234", "")

        val product1 = Product("", "Płyn do naczyń", 10)
        val product2 = Product("", "Proszek do prania", 20)

        val debtor1 = BillDebtor("", 15, "Filip", false)

        val list1 = ShoppingList("", "Pierwsza lista", 30, user , arrayOf(product1, product2), arrayOf(debtor1), "11.06.2019")

        dataList.add(list1)

        adapterShoppingList.notifyDataSetChanged()
    }


    private fun showDetails(shoppingList: ShoppingList) {
        val shoppingListDetailsFragment = ShoppingListDetailsFragment.newInstance(shoppingList)
        (activity as MainActivity).switchFragment(shoppingListDetailsFragment)
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
        adapterShoppingList.notifyDataSetChanged()
    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }


    companion object {
        @JvmStatic
        fun newInstance() = ShoppingListFragment()
    }
}
