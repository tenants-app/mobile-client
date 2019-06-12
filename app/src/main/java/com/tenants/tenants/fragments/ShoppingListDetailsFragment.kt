package com.tenants.tenants.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.tenants.tenants.MainActivity

import com.tenants.tenants.R
import com.tenants.tenants.api.RetrofitClient
import com.tenants.tenants.models.Product
import com.tenants.tenants.models.ShoppingList
import com.tenants.tenants.storage.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_shopping_list_details.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM = "SHOPPING_LIST"

class ShoppingListDetailsFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var currentGroupId: String? = null
    private lateinit var baseContext: Context
    private lateinit var shoppingList: ShoppingList


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            shoppingList = it.getSerializable(ARG_PARAM) as ShoppingList
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =  inflater.inflate(R.layout.fragment_shopping_list_details, container, false)

        currentGroupId = SharedPrefManager.getInstance(baseContext).groupId

        view.shoppingListName.text = shoppingList.name
        view.shopping_list_holder.text = shoppingList.user?.username
        view.shopping_list_holder_bank_number.text = shoppingList.user?.bank_account_number
        view.shoppingListDate.text = shoppingList.createdAt?.subSequence(0, 10)
        view.shopping_list_value.text = shoppingList.value.toString() + " zł"
        view.shopping_list_value_divided.text = shoppingList.debtors!![0].value.toString() + " zł"

        for (product: Product in shoppingList.products) {
            val productText = getProductTextView(product.name, product.value)
            view.addedProductsListLayout.addView(productText)
        }

        view.setAsPaidButton.setOnClickListener {
            setAsPaid()
        }

        if (shoppingList.debtors!![0].paid) {
            view.setAsPaidButton.visibility = View.GONE
            view.toPaidTextView.text = "Zapłacono"
        }

        return view
    }


    private fun getProductTextView(productName: String, productValue: Int): TextView {
        val productTextView = TextView(baseContext)
        productTextView.text = "◦ " + productName + " - " + productValue.toString() + "zł"
        productTextView.setTextSize(18F)

        return productTextView
    }


    private fun setAsPaid() {
        RetrofitClient(baseContext).instance.setShoppingListAsPaid(currentGroupId, shoppingList._id)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() == 200) {
                        Toast.makeText(baseContext, getString(R.string.shopping_list_paid), Toast.LENGTH_LONG).show()
                        val shoppingListFragment = ShoppingListFragment.newInstance()
                        (activity as MainActivity).switchFragment(shoppingListFragment)
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


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: ShoppingList) =
            ShoppingListDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM, param1)
                }
            }
    }
}
