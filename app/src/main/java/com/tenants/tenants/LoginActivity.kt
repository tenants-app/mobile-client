package com.tenants.tenants

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tenants.tenants.api.RetrofitClient
import com.tenants.tenants.models.LoginResponse
import com.tenants.tenants.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.support.v4.content.ContextCompat


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary))

        buttonLogin.setOnClickListener {

            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isEmpty()) {
                editTextEmail.error = getString(R.string.email_required)
                editTextEmail.requestFocus()
                return@setOnClickListener
            }


            if (password.isEmpty()) {
                editTextPassword.error = getString(R.string.password_required)
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient(applicationContext).instance.userLogin(email, password)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        print(response.code())
                        if (response.code() == 200) {

                            SharedPrefManager.getInstance(applicationContext).saveToken(response.body()?.user?.token!!)

                            val intent = Intent(applicationContext, ChooseGroupActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                            startActivity(intent)


                        } else {
                            Toast.makeText(applicationContext, getString(R.string.invalid_credentials), Toast.LENGTH_LONG).show()
                        }

                    }
                })

        }

    }

    override fun onStart() {
        super.onStart()

        if(SharedPrefManager.getInstance(this).isGroupChoosen){
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        } else if(SharedPrefManager.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, ChooseGroupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}
