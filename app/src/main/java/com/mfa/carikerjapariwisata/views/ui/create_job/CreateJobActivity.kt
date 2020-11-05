package com.mfa.carikerjapariwisata.views.ui.create_job

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.mfa.carikerjapariwisata.BuildConfig
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.Jobs
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_create_job.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CreateJobActivity : AppCompatActivity() {
    private lateinit var createJobViewModel: CreateJobViewModel

    private var filePath: String? = null
    private val PICK_PHOTO = 1958
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var mImageUri: Uri? = null

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_job)
        createJobViewModel = ViewModelProviders.of(this).get(CreateJobViewModel::class.java)

        llSelectPhoto.setOnClickListener {
            addPhoto()
        }

        ibSubmit.setOnClickListener {
            val jobs = Jobs(
                job_title = "Test title",
                job_city = "test city",
                job_desc = "test job desc",
                job_date_end = "12-12-2020",
                job_place = "test place",
                job_sallary = "test sallary",
                photo = "ini_photo.jpg"
            )
            createJobViewModel.create_data(jobs, filePath)
        }

        val arrayList: ArrayList<String> = ArrayList()
        arrayList.add("JAVA")
        arrayList.add("ANDROID")
        arrayList.add("C Language")
        arrayList.add("CPP Language")
        arrayList.add("Go Language")
        arrayList.add("AVN SYSTEMS")

        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.setAdapter(arrayAdapter)
        
        etDateEnd.setOnClickListener {
            val newCalendar: Calendar = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                this,
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val newDate: Calendar = Calendar.getInstance()
                    newDate.set(year, monthOfYear, dayOfMonth)
                    val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                etDateEnd.setText(dateFormatter.format(newDate.getTime()))
                },
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }
    }

    private fun createData(jobs: Jobs, file: String?) {
         var mInterface = ApiClient.getClient().create(ApiInterface::class.java)

        val file = File(file)
        //create RequestBody instance from file
        //create RequestBody instance from file
        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            file
        ) //allow image and any other file

        // MultipartBody.Part is used to send also the actual file name

        // MultipartBody.Part is used to send also the actual file name
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        mInterface.crete_job(
            jobs.job_title,
            body,
            jobs.job_place,
            jobs.job_date_end,
            jobs.job_sallary,
            jobs.job_city,
            jobs.job_desc,
            jobs.photo,
            jobs.user_id
        )?.enqueue(object : retrofit2.Callback<ResponseBody?> {

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                Toast.makeText(this@CreateJobActivity, t.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>?) {
                Toast.makeText(this@CreateJobActivity, "Success", Toast.LENGTH_SHORT).show()
            }

        })
//        var _mInterface = ApiClient.getClient().create(ApiInterface::class.java)
//        val file = File(file)
//        //create RequestBody instance from file
//        //create RequestBody instance from file
//        val requestFile: RequestBody = RequestBody.create(
//            MediaType.parse("multipart/form-data"),
//            file
//        ) //allow image and any other file
//
//        // MultipartBody.Part is used to send also the actual file name
//
//        // MultipartBody.Part is used to send also the actual file name
//        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
//        var job = Job()
//
//
//                val callTagihan: retrofit2.Call<ResponseBody?>? =
//                    _mInterface.crete_job(
//                        jobs.job_title,
//                        body,
//                        jobs.job_place,
//                        jobs.job_date_end,
//                        jobs.job_sallary,
//                        jobs.job_city,
//                        jobs.job_desc,
//                        jobs.photo,
//                        jobs.user_id
//                    )
//
//                callTagihan?.enqueue(object: retrofit2.Callback<ResponseBody?>{
//                    override fun onFailure(call: retrofit2.Call<ResponseBody?>?, t: Throwable?) {
////                            _response.value = "Tidak dapat terhubung ke server!"
//                    }
//
//                    override fun onResponse(
//                        call: retrofit2.Call<ResponseBody?>?,
//                        response: Response<ResponseBody?>?
//                    ) {
//
//                    }
//
//            })


    }

    fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have write permission
        val permission: Int = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    fun addPhoto() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAutoZoomEnabled(true)
            .setAspectRatio(1, 1)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setRequestedSize(1280, 1280)
            .start(this)

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                filePath = resultUri.path
                mImageUri = resultUri
                Picasso.with(this).load(mImageUri)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.mipmap.ic_launcher)
                    .into(ivLogo)
                Picasso.with(this).invalidate(mImageUri)
                ivLogo.setPadding(0, 0, 0, 0)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                if (BuildConfig.DEBUG) error.printStackTrace()
            }
        }
    }

}