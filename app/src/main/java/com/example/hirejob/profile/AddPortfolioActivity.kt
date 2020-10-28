package com.example.hirejob.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import com.example.hirejob.R
import com.example.hirejob.databinding.ActivityAddPortfolioBinding
import com.example.hirejob.util.restapi.ApiClient
import com.example.hirejob.util.restapi.portfolio.PortfolioApiService
import com.example.hirejob.util.restapi.portfolio.UpdatePortfolioResponse
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddPortfolioActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAddPortfolioBinding
    private var image: MultipartBody.Part? = null
    private var imgFile: File? = null
    private var typePort = ""

    companion object {
        const val ADD_DATA = 9013
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_portfolio)
        typePort = "Mobile"
        binding.rgType.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.tv_mobile -> {
                    typePort = "Mobile"
                }
                R.id.tv_web -> {
                    typePort = "Web"
                }
            }
        }

        binding.btnBack.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        binding.btnSelectImage.setOnClickListener(this)

    }

    private fun addPortfolioApi() {
        val service = ApiClient.getApiClient(this)!!.create(PortfolioApiService::class.java)
        val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        val fieldMap = HashMap<String, RequestBody>()
        fieldMap["name"] = createPartFromString(binding.edtName.text.toString())
        fieldMap["linkRepo"] = createPartFromString(binding.edtLinkRepo.text.toString())
        fieldMap["typePorto"] = createPartFromString(typePort)
        fieldMap["description"] = createPartFromString(binding.edtDesc.text.toString())

        if (imgFile != null) {
            val exs = imgFile!!.name.split(".")[1]
            val requestFile =
                    imgFile!!.asRequestBody("image/${exs}".toMediaTypeOrNull())
            image =
                    MultipartBody.Part?.createFormData("image", imgFile!!.name, requestFile)
            Log.d("imgFile", imgFile.toString())
            Log.d("image", image.toString())
        }

        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.addPortfolioRequest(fieldMap, image)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is UpdatePortfolioResponse) {
                if (response.success) {
                    Toast.makeText(this@AddPortfolioActivity, response.message, Toast.LENGTH_SHORT)
                            .show()
                } else {
                    Toast.makeText(this@AddPortfolioActivity, response.message, Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
    }

    private fun check(name: String?, linkRepo: String?, desc: String?): Boolean {
        if (name.isNullOrEmpty()) {
            binding.edtName.error = "Enter name portfolio"
            binding.edtName.requestFocus()
            return false
        }
        if (linkRepo.isNullOrEmpty()) {
            binding.edtLinkRepo.error = "Enter link repository"
            binding.edtLinkRepo.requestFocus()
            return false
        }
        if (desc.isNullOrEmpty()) {
            binding.edtDesc.error = "Enter description"
            binding.edtDesc.requestFocus()
            return false
        }
        if (imgFile == null) {
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> finish()
            R.id.btn_save -> {
                val validate = check(
                        binding.edtName.text.toString(),
                        binding.edtLinkRepo.text.toString(),
                        binding.edtDesc.text.toString()
                )
                if (validate) {
                    addPortfolioApi()
                    setResult(RESULT_OK)
                    finish()
                }
            }
            R.id.btn_selectImage -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED
                    ) {
                        //permission denied
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        //show popup to request runtime permission
                        requestPermissions(permissions, PERMISSION_CODE)
                    } else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                } else {
                    //system OS is < Marshmallow
                    pickImageFromGallery()
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            binding.imgPortofolio.setImageURI(data?.data)

            val filePath = getPath(this, data?.data)
            imgFile = File(filePath)
            Log.d("imgFile", imgFile.toString())
            Log.d("filepath", filePath)
        }
    }

    private fun getPath(context: Context, uri: Uri?): String {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
                uri?.let { context.contentResolver.query(it, proj, null, null, null) }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(proj[0])
                result = cursor.getString(column_index)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Not found"
        }
        return result
    }

    @NonNull
    private fun createPartFromString(json: String): RequestBody {
        val mediaType = "text/plain".toMediaType()
        return json
                .toRequestBody(mediaType)
    }
}