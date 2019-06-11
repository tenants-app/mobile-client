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
import kotlinx.android.synthetic.main.fragment_shopping_list_add.*
import kotlinx.android.synthetic.main.fragment_shopping_list_add.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShoppingListAddFragment : Fragment() {
    private lateinit var baseContext: Context
    private var currentGroupId: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var productsList: ArrayList<Product> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_shopping_list_add, container, false)

        currentGroupId = SharedPrefManager.getInstance(baseContext).groupId

        view.addProductButton.setOnClickListener {
            if (productFieldsValid()) {
                addProduct()
            }
        }

        view.saveShoppingListButton.setOnClickListener {
            if (shoppingListNameValid()) {
                saveShoppingList()
            }
        }

        return view
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


    private fun saveShoppingList() {
        val shoppingListName = addShoppingListName.text.toString()
        val shoppingList = ShoppingList(null, shoppingListName, null, null, productsList.toTypedArray(), null, null)

        RetrofitClient(baseContext).instance.addNewShoppingList(currentGroupId, shoppingList)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() == 200) {
                        Toast.makeText(baseContext, getString(R.string.shopping_list_saved), Toast.LENGTH_LONG).show()
                        val shoppingListFragment = ShoppingListFragment.newInstance()
                        (activity as MainActivity).switchFragment(shoppingListFragment)
                    } else {
                        Toast.makeText(baseContext, response.message(), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }


    private fun addProduct() {
        val productName = addProductName.text.toString()
        val productValue = addProductValue.text.toString().toInt()

        val product = Product(null, productName, productValue)
        productsList.add(product)

        val productTextView = getProductTextView(productName, productValue)
        addedProductsListLayout.addView(productTextView)
    }


    private fun getProductTextView(productName: String, productValue: Int): TextView {
        val productTextView = TextView(baseContext)
        productTextView.text = "● " + productName + " - " + productValue.toString() + "zł"
        productTextView.setTextSize(18F)

        return productTextView
    }


    private fun productFieldsValid(): Boolean {
        val productName = addProductName.text.toString()
        val productValue = addProductValue.text.toString()

        if (productName.isEmpty()) {
            addProductName.error = getString(R.string.debt_name_required)
            addProductName.requestFocus()
            return false
        }

        if (productValue.isEmpty()) {
            addProductValue.error = getString(R.string.debt_value_required)
            addProductValue.requestFocus()
            return false
        }

        return true
    }


    private fun shoppingListNameValid() : Boolean {
        val shoppingListName = addShoppingListName.text.toString()

        if (shoppingListName.isEmpty()) {
            addShoppingListName.error = getString(R.string.debt_name_required)
            addShoppingListName.requestFocus()
            return false
        }

        return true
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
        fun newInstance() = ShoppingListAddFragment()
    }
}
