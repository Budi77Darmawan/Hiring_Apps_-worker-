package com.example.hirejob.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hirejob.R
import com.example.hirejob.databinding.ActivityEditExperienceBinding
import com.example.hirejob.home.detailviewpager.ExperienceModel
import com.example.hirejob.util.restapi.ApiClient
import com.example.hirejob.util.restapi.experience.ExperienceApiService
import com.example.hirejob.util.restapi.experience.UpdateExperienceResponse
import kotlinx.coroutines.*

class EditExperienceActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityEditExperienceBinding
    private var data: ExperienceModel? = null

    companion object {
        const val EXTRA_DATA = "data_experience"
        const val UPDATE_DATA = 9013
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_experience)

        data = intent.getParcelableExtra(EXTRA_DATA)

        binding.apply {
            edtPosition.setText(data?.job, TextView.BufferType.EDITABLE)
            edtCompany.setText(data?.company, TextView.BufferType.EDITABLE)
            edtStart.setText(data?.start?.split("T")?.get(0), TextView.BufferType.EDITABLE)
            edtEnd.setText(data?.end?.split("T")?.get(0), TextView.BufferType.EDITABLE)
            edtDesc.setText(data?.description, TextView.BufferType.EDITABLE)
        }

        binding.btnBack.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
    }

    private fun updateExperienceApi() {
        val service = ApiClient.getApiClient(this)!!.create(ExperienceApiService::class.java)
        val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        val fieldMap = HashMap<String, String>()
        fieldMap["position"] = binding.edtPosition.text.toString()
        fieldMap["companyName"] = binding.edtCompany.text.toString()
        fieldMap["description"] = binding.edtDesc.text.toString()
        fieldMap["start"] = binding.edtStart.text.toString()
        fieldMap["end"] = binding.edtEnd.text.toString()

        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.updateExperienceRequest(data?.id_exp, fieldMap)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is UpdateExperienceResponse) {
                if (response.success) {
                    Toast.makeText(this@EditExperienceActivity, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> finish()
            R.id.btn_save -> {
                val validate = check(
                        binding.edtPosition.text.toString(),
                        binding.edtCompany.text.toString(),
                        binding.edtDesc.text.toString(),
                        binding.edtStart.text.toString(),
                        binding.edtEnd.text.toString()
                )
                if (validate) {
                    updateExperienceApi()
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }

    private fun check(position: String?, companyName: String?, description: String?, start: String?, end: String?): Boolean {
        if (position.isNullOrEmpty()) {
            binding.edtPosition.error = "Enter position"
            binding.edtPosition.requestFocus()
            return false
        }
        if (companyName.isNullOrEmpty()) {
            binding.edtCompany.error = "Enter company name"
            binding.edtCompany.requestFocus()
            return false
        }
        if (description.isNullOrEmpty()) {
            binding.edtDesc.error = "Enter description"
            binding.edtDesc.requestFocus()
            return false
        }
        if (start.isNullOrEmpty()) {
            binding.edtStart.error = "Enter start work"
            binding.edtStart.requestFocus()
            return false
        }
        if (end.isNullOrEmpty()) {
            binding.edtEnd.error = "Confirm end work"
            binding.edtEnd.requestFocus()
            return false
        }
        return true
    }

}