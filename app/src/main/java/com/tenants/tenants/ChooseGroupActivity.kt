package com.tenants.tenants

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.Toast
import com.tenants.tenants.api.RetrofitClient
import com.tenants.tenants.models.Group
import com.tenants.tenants.models.GroupsResponse
import com.tenants.tenants.storage.SharedPrefManager
import kotlinx.android.synthetic.main.empty_groups_view.*
import kotlinx.android.synthetic.main.new_group_dialog.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseGroupActivity : AppCompatActivity() {

    private lateinit var adapterGroup: GroupRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private var dataList: ArrayList<Group> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_group)

        supportActionBar!!.setTitle(R.string.select_group)

        recyclerView = findViewById(R.id.recycler_view)
        adapterGroup = GroupRecyclerViewAdapter(this, dataList, onClickListener = { group -> onGroupChoose(group)})
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = adapterGroup

        getUserGroups()
    }

    private fun getUserGroups() {
        RetrofitClient(applicationContext).instance.getUserGroups()
            .enqueue(object : Callback<GroupsResponse> {
                override fun onFailure(call: Call<GroupsResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<GroupsResponse>, response: Response<GroupsResponse>) {
                    if (response.code() == 200) {
                        if (response.body()!!.groups.isEmpty()) {
                            setContentView(R.layout.empty_groups_view)
                            connectListeners()
                        }
                        for (group: Group in response.body()!!.groups) {
                            dataList.add(group)
                        }
                    } else {
                        Toast.makeText(applicationContext, getString(R.string.cannot_get_groups), Toast.LENGTH_LONG).show()
                    }
                    adapterGroup.notifyDataSetChanged()
                }
            })
    }

    private fun connectListeners() {
        logout_button.setOnClickListener {
            SharedPrefManager.getInstance(applicationContext).clear()

            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        add_group_button.setOnClickListener {
            showAddGroupDialog()
        }

    }

    fun showAddGroupDialog() {
        val newGroupDialogView = LayoutInflater.from(this).inflate(R.layout.new_group_dialog, null)

        val mBuilder = AlertDialog.Builder(this)
            .setView(newGroupDialogView)
            .setTitle("Nowa grupa")

        val  addGroupDialog = mBuilder.show()

        newGroupDialogView.dialogGroupAddButton.setOnClickListener {

            val groupName = newGroupDialogView.groupName.text.toString()

            if (groupName.isEmpty()) {
                newGroupDialogView.groupName.error = getString(R.string.debt_name_required)
                newGroupDialogView.groupName.requestFocus()
                return@setOnClickListener
            }

            addGroupDialog.dismiss()
            addNewGroup(groupName)
        }

        newGroupDialogView.dialogGroupCancelButton.setOnClickListener {
            addGroupDialog.dismiss()
        }
    }


    fun addNewGroup(groupName: String) {
        RetrofitClient(applicationContext).instance.addNewGroup(groupName)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() == 200) {
                        Toast.makeText(applicationContext, getString(R.string.group_added), Toast.LENGTH_LONG).show()
                        recreate()
                    }
                }
            })
    }


    private fun onGroupChoose(group: Group) {
        SharedPrefManager.getInstance(applicationContext).saveGroup(group)

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }
}
