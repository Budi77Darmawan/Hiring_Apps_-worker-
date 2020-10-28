package com.example.hirejob.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.hirejob.R
import com.example.hirejob.databinding.FragmentLogoutDialogBinding
import com.example.hirejob.login.LoginActivity
import com.example.hirejob.util.sharedpreferences.Constant
import com.example.hirejob.util.sharedpreferences.SharedPrefProvider

class LogoutDialog : DialogFragment(), View.OnClickListener {
    private lateinit var binding: FragmentLogoutDialogBinding
    private lateinit var sharedPref: SharedPrefProvider

    companion object {
        val TAG = LogoutDialog::class.java.simpleName
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        sharedPref = SharedPrefProvider(requireContext())
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_logout_dialog, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isCancelable = false
        binding.tvYes.setOnClickListener(this)
        binding.tvCancel.setOnClickListener(this)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_yes-> {
                sharedPref.resetSharedPref()
                sharedPref.putBoolean(Constant.KEY_ONBOARD, true)
                val intent = Intent (activity, LoginActivity::class.java)
                activity?.startActivity(intent)
                activity?.finish()
            }
            R.id.tv_cancel -> {
                dismiss()
            }
        }
    }
}
