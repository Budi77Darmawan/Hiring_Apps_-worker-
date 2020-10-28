package com.example.hirejob.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hirejob.R
import com.example.hirejob.databinding.ActivityRegisterBinding
import com.example.hirejob.login.LoginActivity
import com.example.hirejob.util.restapi.ApiClient
import com.example.hirejob.util.restapi.account.AccountApiService

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        val service =
            ApiClient.getApiClient(this)?.create(AccountApiService::class.java)
        if (service != null) {
            viewModel.setAccountService(service)
        }

        binding.btnRegister.setOnClickListener(this)
        binding.tvLogin.setOnClickListener(this)
        binding.login.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_register -> {
                val check = check(
                    binding.edtName.text.toString(),
                    binding.edtEmail.text.toString(),
                    binding.edtNumberphone.text.toString(),
                    binding.edtPassword.text.toString(),
                    binding.edtConfirmpassword.text.toString()
                )
                if (check) {
                    viewModel.registerAccountApi(
                        binding.edtName.text.toString(),
                        binding.edtEmail.text.toString(),
                        binding.edtNumberphone.text.toString(),
                        binding.edtPassword.text.toString(),
                    )
                    subscribeLiveData()
                }
            }
            R.id.tv_login -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            R.id.login -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun check(name: String?, email: String?, numberPhone: String?, password: String?, confirmpass: String?): Boolean {
        if (name.isNullOrEmpty()) {
            binding.edtName.error = "Enter fullname"
            binding.edtName.requestFocus()
            return false
        }
        if (email.isNullOrEmpty()) {
            binding.edtEmail.error = "Enter email"
            binding.edtEmail.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.error = "Invalid email"
            binding.edtEmail.requestFocus()
            return false
        }
        if (numberPhone.isNullOrEmpty()) {
            binding.edtNumberphone.error = "Enter numberphone"
            binding.edtNumberphone.requestFocus()
            return false
        }
        if (password.isNullOrEmpty()) {
            binding.edtPassword.error = "Enter password"
            binding.edtPassword.requestFocus()
            return false
        }
        if (confirmpass.isNullOrEmpty()) {
            binding.edtConfirmpassword.error = "Confirm password"
            binding.edtConfirmpassword.requestFocus()
            return false
        }
        if (password != confirmpass) {
            binding.edtConfirmpassword.error = "Password mismatch"
            binding.edtConfirmpassword.requestFocus()
            return false
        }
        return true
    }

    private fun subscribeLiveData() {
        viewModel.registerLiveData.observe(this, {
            if (it) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        })

        viewModel.loadingLiveData.observe(this, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.messageLiveData.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }
}