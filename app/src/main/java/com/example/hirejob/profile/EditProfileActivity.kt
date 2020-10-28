package com.example.hirejob.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.hirejob.R
import com.example.hirejob.databinding.ActivityEditProfileBinding
import com.example.hirejob.home.DetailFreelancersActivity
import com.example.hirejob.home.FreelancersModel
import com.example.hirejob.home.detailviewpager.GridSkillAdapter
import com.example.hirejob.util.restapi.ApiClient
import com.example.hirejob.util.restapi.account.AccountApiService
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

class EditProfileActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel
    private var data: FreelancersModel? = null
    private var imgFile: File? = null
    private lateinit var job: String
    private lateinit var status: String

    companion object {
        const val EXTRA_DATA = "data_freelancers"
        const val UPDATE_CODE = 9013
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)

        data = intent.getParcelableExtra(DetailFreelancersActivity.EXTRA_DATA)
        job = data?.jobDesc.toString()
        status = data?.statusJob.toString()

        binding.let {
            it.edtName.setText(data?.name, TextView.BufferType.EDITABLE)
            it.edtDesc.setText(data?.description, TextView.BufferType.EDITABLE)
            it.edtNumberPhone.setText(data?.numberPhone, TextView.BufferType.EDITABLE)
            it.edtAddress.setText(data?.cityAddress, TextView.BufferType.EDITABLE)
            if (!data?.image.isNullOrEmpty()) {
                Glide.with(this)
                        .load("http://18.212.194.218:8080/images/${data?.image}")
                        .into(it.imgProfil)
            }
        }

        viewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)
        val service = ApiClient.getApiClient(this)?.create(AccountApiService::class.java)
        if (service != null) {
            viewModel.setAccountService(service)
        }
        subscribeLiveData()

        ArrayAdapter.createFromResource(
                this,
                R.array.job_list,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerJob.adapter = adapter
            val spinnerPosition: Int = adapter.getPosition(job)
            binding.spinnerJob.setSelection(spinnerPosition)
        }
        binding.spinnerJob.onItemSelectedListener = this

        ArrayAdapter.createFromResource(
                this,
                R.array.status_list,
                android.R.layout.simple_spinner_item
        ).also { adapter1 ->
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerStatus.adapter = adapter1
            val spinnerPosition: Int = adapter1.getPosition(status)
            binding.spinnerStatus.setSelection(spinnerPosition)
        }
        binding.spinnerStatus.onItemSelectedListener = this

        if (data?.skill != null) {
            val arrSkill = data!!.skill!!.split(",")
            showRecyclerGrid(arrSkill)
        }

        binding.btnSave.setOnClickListener(this)
        binding.btnSelectImage.setOnClickListener(this)
    }

    private fun showRecyclerGrid(list: List<String>) {
        binding.rvSkill.layoutManager = GridLayoutManager(this, 4)
        val gridSkillAdapter = GridSkillAdapter(list)
        binding.rvSkill.adapter = gridSkillAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(
                requestCode,
                resultCode,
                data,
                this,
                object : DefaultCallback() {
                    override fun onImagePicked(
                            imageFile: File,
                            source: EasyImage.ImageSource,
                            type: Int
                    ) {
                        CropImage.activity(Uri.fromFile(imageFile))
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .setFixAspectRatio(true)
                                .start(this@EditProfileActivity)
                    }

                    override fun onImagePickerError(
                            e: Exception,
                            source: EasyImage.ImageSource,
                            type: Int
                    ) {
                        super.onImagePickerError(e, source, type)
                        Toast.makeText(this@EditProfileActivity, e.message, Toast.LENGTH_SHORT)
                                .show()
                    }
                })

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val uri = result.uri
                Glide.with(applicationContext)
                        .load(File(uri.path))
                        .into(binding.imgProfil)
                imgFile = File(uri.path)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val exception = result.error
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateAccount() {
        viewModel.updateAccountApi(
                binding.edtName.text.toString(),
                binding.edtNumberPhone.text.toString()
        )
    }

    private fun updateFreelancers() {
        viewModel.updateFreelancersApi(
                job,
                status,
                binding.edtDesc.text.toString(),
                binding.edtAddress.text.toString(),
                imgFile
        )
    }

    private fun subscribeLiveData() {
        viewModel.messageLiveData.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_save -> {
                updateAccount()
                updateFreelancers()
                setResult(RESULT_OK)
                finish()
            }
            R.id.btn_selectImage -> {
                EasyImage.openChooserWithGallery(this, "Select Image", 3)
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        if (parent != null) {
            if (parent.id == R.id.spinner_job) {
                when (position) {
                    0 -> job = "Android Developer"
                    1 -> job = "Web Developer"
                    2 -> job = "IOS Developer"
                }
            } else if (parent.id == R.id.spinner_status) {
                when (position) {
                    0 -> status = "Freelancers"
                    1 -> status = "Full Time"
                    2 -> status = "Part Time"
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}