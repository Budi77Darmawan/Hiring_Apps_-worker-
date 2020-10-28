package com.example.hirejob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.hirejob.databinding.ActivityOptionBinding
import com.example.hirejob.login.LoginActivity
import com.example.hirejob.register.RegisterActivity
import com.example.hirejob.util.sharedpreferences.Constant
import com.example.hirejob.util.sharedpreferences.SharedPrefProvider

class OptionActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityOptionBinding
    private lateinit var sharedPref: SharedPrefProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_option)
        sharedPref = SharedPrefProvider(this)

        val onBoard = sharedPref.getBoolean(Constant.KEY_ONBOARD)
        val remember = sharedPref.getBoolean(Constant.KEY_REMEMBER)
        if (onBoard && remember) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else if (onBoard) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                sharedPref.putBoolean(Constant.KEY_ONBOARD, true)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            R.id.btn_register -> {
                sharedPref.putBoolean(Constant.KEY_ONBOARD, true)
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            }
        }
    }
}