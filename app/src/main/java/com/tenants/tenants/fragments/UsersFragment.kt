package com.tenants.tenants.fragments

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast

import com.tenants.tenants.R
import com.tenants.tenants.api.ActivationLinkResponse
import com.tenants.tenants.api.RetrofitClient
import com.tenants.tenants.storage.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_users.view.*
import kotlinx.android.synthetic.main.new_user_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UsersFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var baseContext: Context
    private var currentGroupId: String? = null
    private lateinit var newUserDialogView: View
    var mobileArray = arrayOf("Android", "IPhone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_users, container, false)

        currentGroupId = SharedPrefManager.getInstance(baseContext).groupId

        val adapter = ArrayAdapter<String>(baseContext, R.layout.user_item, mobileArray)

        view.usersList.setAdapter(adapter);

        view.addUserButton.setOnClickListener {
            showDialog()
        }

        return view
    }


    fun showDialog() {
        newUserDialogView = LayoutInflater.from(baseContext).inflate(R.layout.new_user_dialog, null)

        val mBuilder = AlertDialog.Builder(baseContext)
            .setView(newUserDialogView)
            .setTitle("Nowy użytkownik")

        val  mAlertDialog = mBuilder.show()

        newUserDialogView.dialogGenerateActivationLinkButton.setOnClickListener {

            val userEmail = newUserDialogView.dialogUserEmail.text.toString()

            if (userEmail.isEmpty()) {
                newUserDialogView.dialogUserEmail.error = getString(R.string.email_required)
                newUserDialogView.dialogUserEmail.requestFocus()
                return@setOnClickListener
            }

            addNewUser(userEmail)
        }

        newUserDialogView.dialogCopyClipBoardButton.setOnClickListener {
            Toast.makeText(baseContext, R.string.copied_to_clipboard, Toast.LENGTH_LONG).show()
        }

        newUserDialogView.dialogSendLinkOnEmailButton.setOnClickListener {
            Toast.makeText(baseContext, R.string.email_has_been_sent, Toast.LENGTH_LONG).show()
        }

        newUserDialogView.dialogCancelButton.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }


    fun addNewUser(email: String) {
        RetrofitClient(baseContext).instance.generateActivationLink(email, currentGroupId)
            .enqueue(object : Callback<ActivationLinkResponse> {
                override fun onFailure(call: Call<ActivationLinkResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ActivationLinkResponse>, response: Response<ActivationLinkResponse>) {
                    if (response.code() == 200) {
                        newUserDialogView.activationLinkLayout.visibility = View.VISIBLE
                        newUserDialogView.activationLinkTextView.text = response.body()?.link
                    } else {
                        Toast.makeText(baseContext, response.message(), Toast.LENGTH_LONG).show()
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

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance() = UsersFragment()
    }
}
