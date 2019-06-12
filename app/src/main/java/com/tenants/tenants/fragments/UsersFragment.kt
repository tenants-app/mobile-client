package com.tenants.tenants.fragments

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
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
import com.tenants.tenants.api.MembersResponse
import com.tenants.tenants.api.RetrofitClient
import com.tenants.tenants.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_users.view.*
import kotlinx.android.synthetic.main.new_user_dialog.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class UsersFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var currentGroupId: String? = null
    private var membersList: ArrayList<String> = ArrayList()
    private lateinit var baseContext: Context
    private lateinit var newUserDialogView: View
    private lateinit var adapterUsers: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_users, container, false)

        activity!!.nav_view.setCheckedItem(R.id.sidebar_apartment)

        currentGroupId = SharedPrefManager.getInstance(baseContext).groupId

        adapterUsers = ArrayAdapter(baseContext, R.layout.user_item, membersList)

        view.usersList.setAdapter(adapterUsers);

        view.addUserButton.setOnClickListener {
            showDialog()
        }

        getGroupMembers()

        return view
    }


    fun getGroupMembers() {
        RetrofitClient(baseContext).instance.getGroupMembers(currentGroupId)
            .enqueue(object : Callback<MembersResponse> {
                override fun onFailure(call: Call<MembersResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<MembersResponse>, response: Response<MembersResponse>) {
                    if (response.code() == 200) {
                        for (member in response.body()!!.members) {
                            membersList.add(member.username)
                        }
                        adapterUsers.notifyDataSetChanged()
                    } else {
                        Toast.makeText(baseContext, response.message(), Toast.LENGTH_LONG).show()
                    }
                }
            })
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
            val activationLink = newUserDialogView.activationLinkTextView.text.toString()

            val clipboard = baseContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("activation_link", activationLink)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(baseContext, R.string.copied_to_clipboard, Toast.LENGTH_LONG).show()
        }

        newUserDialogView.dialogSendLinkOnEmailButton.setOnClickListener {
            val activationLink = newUserDialogView.activationLinkTextView.text.toString()
            val userEmail = newUserDialogView.dialogUserEmail.text.toString()

            sendEmail(userEmail, activationLink)
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


    fun sendEmail(email: String, invitationLink: String) {
        RetrofitClient(baseContext).instance.sendActivationLink(email, invitationLink)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() == 200) {
                        Toast.makeText(baseContext, R.string.email_has_been_sent, Toast.LENGTH_LONG).show()
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
