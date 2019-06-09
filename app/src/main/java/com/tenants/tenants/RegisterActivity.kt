package com.tenants.tenants

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.tenants.tenants.api.RetrofitClient
import com.tenants.tenants.models.RegisterResponse
import com.tenants.tenants.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.theme_light))
        connectListeners()
    }

    private fun connectListeners() {

        buttonRegister.setOnClickListener {

            val username = registerEditTextUsername.text.toString().trim()
            val email = registerEditTextEmail.text.toString().trim()
            val password = registerEditTextPassword.text.toString().trim()
            val bankAccountNumber = registerEditTextBankAccountNumber.text.toString().trim()

            if(!validateFields(username, email, password, bankAccountNumber)) {
                return@setOnClickListener
            }

            RetrofitClient(applicationContext).instance.userRegister(username, email, password, bankAccountNumber)
                .enqueue(object : Callback<RegisterResponse> {
                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        print(response.code())
                        if (response.code() == 200) {

                            SharedPrefManager.getInstance(applicationContext).saveToken(response.body()?.user?.token!!)

                            val intent = Intent(applicationContext, ChooseGroupActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                            startActivity(intent)


                        } else {
                            Toast.makeText(applicationContext, getString(R.string.invalid_register_data), Toast.LENGTH_LONG).show()
                        }

                    }
                })
        }
    }

    private fun validateFields(username: String, email: String, password: String, bankAccountNumber: String): Boolean {

        if (username.isEmpty()) {
            registerEditTextUsername.error = getString(R.string.username_required)
            registerEditTextUsername.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            registerEditTextEmail.error = getString(R.string.email_required)
            registerEditTextEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            registerEditTextPassword.error = getString(R.string.password_required)
            registerEditTextPassword.requestFocus()
            return false
        }

        if (bankAccountNumber.isEmpty()) {
            registerEditTextBankAccountNumber.error = getString(R.string.bank_account_number_required)
            registerEditTextBankAccountNumber.requestFocus()
            return false
        }

        return true
    }
}
