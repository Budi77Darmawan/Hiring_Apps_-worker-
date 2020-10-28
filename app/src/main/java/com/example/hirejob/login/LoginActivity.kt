package com.example.hirejob.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hirejob.MainActivity
import com.example.hirejob.R
import com.example.hirejob.register.RegisterActivity
import com.example.hirejob.databinding.ActivityLoginBinding
import com.example.hirejob.util.restapi.ApiClient
import com.example.hirejob.util.restapi.account.AccountApiService

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private var showHideBtn = "hide"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        val service =
            ApiClient.getApiClient(this)?.create(AccountApiService::class.java)
        if (service != null) {
            viewModel.setAccountService(service)
        }

        binding.btnLogin.setOnClickListener(this)
        binding.register.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)
        binding.btnShowhidepassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                val check =
                    check(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
                if (check) {
                    viewModel.loginAccountApi(
                        this,
                        binding.edtEmail.text.toString(),
                        binding.edtPassword.text.toString()
                    )
                    subscribeLiveData()
                }
            }
            R.id.tv_register -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
            R.id.register -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
            R.id.btn_showhidepassword -> {
                if (showHideBtn == "show") {
                    binding.edtPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    showHideBtn = "hide"
                } else {
                    binding.edtPassword.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    showHideBtn = "show"
                }
            }
        }
    }

    private fun check(email: String?, password: String?): Boolean {
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
        if (password.isNullOrEmpty()) {
            binding.edtPassword.error = "Enter password"
            binding.edtPassword.requestFocus()
            return false
        }
        return true
    }

    private fun subscribeLiveData() {
        viewModel.loginLiveData.observe(this, {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
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