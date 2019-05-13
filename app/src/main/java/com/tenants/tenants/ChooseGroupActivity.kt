package com.tenants.tenants

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.tenants.tenants.api.RetrofitClient
import com.tenants.tenants.models.Group
import com.tenants.tenants.models.GroupsResponse
import com.tenants.tenants.models.Model
import com.tenants.tenants.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseGroupActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecyclerViewAdapter
    var dataList:ArrayList<Model> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_group)

        supportActionBar!!.setTitle(R.string.select_group)

        recyclerView = findViewById(R.id.recycler_view)
        adapter = RecyclerViewAdapter(this, dataList, onClickListener = { groupId -> onGroupChoose(groupId)})
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = adapter

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
                        for (group: Group in response.body()!!.groups) {
                            dataList.add(group)
                        }
                    } else {
                        Toast.makeText(applicationContext, getString(R.string.cannot_get_groups), Toast.LENGTH_LONG).show()
                    }
                    adapter.notifyDataSetChanged()

                }
            })
    }



    private fun onGroupChoose(groupId: String) {
        SharedPrefManager.getInstance(applicationContext).saveGroup(groupId)

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }
}