package com.mfa.carikerjapariwisata.views.ui.edit_job

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.mfa.carikerjapariwisata.BuildConfig
import com.mfa.carikerjapariwisata.MainActivity
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.model.Jobs
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_edit_job.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditJobActivity : AppCompatActivity(), EditJobView {
    private lateinit var presenter: EditJobPresenter
    private lateinit var jobs: Jobs
    private var filePath: String? = null
    private var fileName: String? = null
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var mImageUri: Uri? = null

    companion object{
        const val EXTRA_JOB = "extra_job"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_job)

        presenter = EditJobPresenter(this)
        onAttachView()

        jobs = intent.getParcelableExtra<Jobs>(EXTRA_JOB)


        Picasso.with(this).load(ApiClient.JOB_URL + (jobs.photo))
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .error(R.drawable.ic_image)
            .into(ivLogo)

        if(jobs.photo == ""){
            tvLogoName.setText("Tambah Logo")
        }else{
            Picasso.with(this).invalidate(ApiClient.JOB_URL+jobs.photo)
            tvLogoName.setText(jobs.photo)
            ivLogo.setPadding(0, 0, 0, 0)
        }

        etTitle.setText(jobs.job_title)
        etPlace.setText(jobs.job_place)
        etDateEnd.setText(jobs.job_date_end)
        etSallary.setText(jobs.job_sallary)
        etDesc.setText(jobs.job_desc)

        llSelectPhoto.setOnClickListener {
            addPhoto()
        }

        ibSubmit.setOnClickListener {
            validate_field()
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
            showCalendarDialog()
        }

        verifyStoragePermissions(this)
    }

    fun showCalendarDialog() {
        val newCalendar: Calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
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

    fun validate_field(){
        if(tvLogoName.text.toString() == "Tambah Logo"){
            tvLogoName.setError("Pilih Foto")
            return
        }
        if(TextUtils.isEmpty(etTitle.text)){
            etTitle.setError("Kolom judul harus diisi!")
            return
        }

        if(TextUtils.isEmpty(etPlace.text)){
            etPlace.setError("Kolom nama tempat pariwisata harus diisi!")
            return
        }

        if(TextUtils.isEmpty(etDateEnd.text)){
            etDateEnd.setError("Kolom batas lowongan harus diisi!")
            return
        }

        if(TextUtils.isEmpty(etSallary.text)){
            etSallary.setError("Kolom kisaran gaji harus diisi!")
            return
        }

        if(TextUtils.isEmpty(etDesc.text)){
            etDesc.setError("Kolom deskripsi pekerjaan harus diisi!")
            return
        }

        if(!TextUtils.isEmpty(etTitle.text) && !TextUtils.isEmpty(etPlace.text) && !TextUtils.isEmpty(etDateEnd.text) && !TextUtils.isEmpty(etSallary.text) && !TextUtils.isEmpty(etDesc.text)){
            val jobs_data = Jobs(
                job_title = etTitle.text.toString(),
                job_city = "test city",
                job_desc = etDesc.text.toString(),
                job_date_end = etDateEnd.text.toString(),
                job_place = etPlace.text.toString(),
                job_sallary = etSallary.text.toString(),
                photo = if (fileName != null) fileName else jobs.photo
            )
            presenter.editJob(jobs.id, jobs_data, filePath)
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
                fileName = filePath?.lastIndexOf("/")?.plus(1)?.let { filePath?.substring(it) }
                mImageUri = resultUri
                Picasso.with(this).load(mImageUri)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.mipmap.ic_launcher)
                    .into(ivLogo)
                Picasso.with(this).invalidate(mImageUri)
                tvLogoName.text = fileName
                ivLogo.setPadding(0, 0, 0, 0)
                tvLogoName.setError(null)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                if (BuildConfig.DEBUG) error.printStackTrace()
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSuccess() {
        val intent  = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onFailed(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun onAttachView() {
        presenter.onAttach(this)
    }

    override fun onDetachView() {
        presenter.onDetach()
    }

    override fun onDestroy() {
        onDetachView()
        super.onDestroy()
    }

}