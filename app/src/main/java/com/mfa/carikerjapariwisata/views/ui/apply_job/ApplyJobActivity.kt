package com.mfa.carikerjapariwisata.views.ui.apply_job

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.utils.FilePath
import com.mfa.carikerjapariwisata.utils.GlobalFunction
import kotlinx.android.synthetic.main.activity_apply_job.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class ApplyJobActivity : AppCompatActivity(), ApplyJobView {
    private lateinit var presenter: ApplyJobPresenter
    private lateinit var globalFunction: GlobalFunction
    private var selectedFilePaths: ArrayList<String> = ArrayList()
    private var selectedImageName: String? = null
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    companion object{
        const val REQUEST_CODE_PICK_FILE1 = 101
        const val REQUEST_CODE_PICK_FILE2 = 102
        const val REQUEST_CODE_PICK_FILE3 = 103
        const val REQUEST_CODE_PICK_FILE4 = 104
        const val REQUEST_CODE_PICK_FILE5 = 105
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_job)

        presenter = ApplyJobPresenter(this)
        onAttachView()

        globalFunction = GlobalFunction(this)

        addAttachment.setOnClickListener {
            if(attachment4.isVisible){
                validation_attachment(tvAttachment4, attachment5)
                addAttachment.setVisibility(View.GONE)
            }else if(attachment3.isVisible){
                validation_attachment(tvAttachment3, attachment4)
            }else if(attachment2.isVisible){
                validation_attachment(tvAttachment2, attachment3)
            }else if(attachment1.isVisible){
                validation_attachment(tvAttachment1, attachment2)
            }
        }

        attachment1.setOnClickListener {
            openImageChooser(REQUEST_CODE_PICK_FILE1)
        }

        attachment2.setOnClickListener {
            openImageChooser(REQUEST_CODE_PICK_FILE2)
        }

        attachment3.setOnClickListener {
            openImageChooser(REQUEST_CODE_PICK_FILE3)
        }

        attachment4.setOnClickListener {
            openImageChooser(REQUEST_CODE_PICK_FILE4)
        }

        attachment5.setOnClickListener {
            openImageChooser(REQUEST_CODE_PICK_FILE5)
        }

        ibSubmit.setOnClickListener {
            validationField()
        }
    }

    private fun validationField(){
        if(TextUtils.isEmpty(etEmail.text)){
            etEmail.setError("Kolom email harus diisi!")
            return
        }
        if(TextUtils.isEmpty(etTelp.text)){
            etTelp.setError("Kolom nomor telepon harus diisi!")
            return
        }
        if(tvAttachment1.text == "Lampiran"){
            globalFunction.createSnackBar(layout, "Pilih setidaknya 1 lampiran!", R.color.green, "warning")
        }

        if(!TextUtils.isEmpty(etEmail.text) && !TextUtils.isEmpty(etTelp.text) && tvAttachment1.text != "Lampiran"){
            val attacments: ArrayList<MultipartBody.Part> = ArrayList()

            for (i in 0 until selectedFilePaths.size) {
                prepareFilePart("attachments["+i+"]",selectedFilePaths.get(i))?.let { it1 -> attacments.add(it1) }
            }

            val data = mapOf<String, Any>(
                "email" to etEmail.text.toString(),
                "no_telp" to etTelp.text.toString(),
                "applicant_desc" to etDesc.text.toString(),
                "job_id" to intent.getStringExtra("job_id"),
                "attachments" to attacments
            )

            presenter.applyJob(data)
        }
    }

    private fun verifyStoragePermissions(activity: Activity?) {
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

    private fun validation_attachment(tvAttachment: TextView, attacment: LinearLayout){
        if(tvAttachment.text == "Lampiran"){
            globalFunction.createSnackBar(layout, "Pilih terlebih dahulu lampiran sebelumnya sebelum menambah lampiran!", R.color.green, "warning")
        }else{
            attacment.setVisibility(View.VISIBLE)
        }
    }

    private fun openImageChooser(requestCode: Int) {
        verifyStoragePermissions(this)

        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "*/*"
            val mimeTypes = arrayOf("image/jpeg, image/png, application/msword,application/pdf")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, requestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            val selectedFileUri = data?.data

            when (requestCode) {
                REQUEST_CODE_PICK_FILE1 -> {
                    assignToArrayList(0, selectedFileUri)
                    selectedImageName = selectedFilePaths.get(0).lastIndexOf("/").plus(1).let { selectedFilePaths.get(0).substring(it) }
                    tvAttachment1.text = selectedImageName
                }
                REQUEST_CODE_PICK_FILE2 -> {
                    assignToArrayList(1, selectedFileUri)
                    selectedImageName = selectedFilePaths.get(1)?.lastIndexOf("/")?.plus(1)?.let { selectedFilePaths.get(1)?.substring(it) }
                    tvAttachment2.text = selectedImageName
                }
                REQUEST_CODE_PICK_FILE3 -> {
                    assignToArrayList(2, selectedFileUri)
                    selectedImageName = selectedFilePaths.get(2)?.lastIndexOf("/")?.plus(1)?.let { selectedFilePaths.get(2)?.substring(it) }
                    tvAttachment3.text = selectedImageName
                }
                REQUEST_CODE_PICK_FILE4 -> {
                    assignToArrayList(3, selectedFileUri)
                    selectedImageName = selectedFilePaths.get(3)?.lastIndexOf("/")?.plus(1)?.let { selectedFilePaths.get(3)?.substring(it) }
                    tvAttachment4.text = selectedImageName
                }
                REQUEST_CODE_PICK_FILE5 -> {
                    assignToArrayList(4, selectedFileUri)
                    selectedImageName = selectedFilePaths.get(4)?.lastIndexOf("/")?.plus(1)?.let { selectedFilePaths.get(4)?.substring(it) }
                    tvAttachment5.text = selectedImageName
                }
            }
        }
    }

    private fun assignToArrayList(index: Int, selectedFileUri: Uri?){
        try {
            if (selectedFileUri != null) {
                FilePath.getPath(this, selectedFileUri)?.let { selectedFilePaths.set(index, it) }
            }
        } catch (e: IndexOutOfBoundsException) {
            if (selectedFileUri != null) {
                FilePath.getPath(this, selectedFileUri)?.let { selectedFilePaths.add(it) }
            }
        }
    }

    private fun prepareFilePart(fileName : String?, file: String?): MultipartBody.Part? {
        val file = File(file)

        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            file
        )

        val body = MultipartBody.Part.createFormData(fileName, file.name, requestFile)
        return body
    }

    override fun onSuccess(message: String) {
        globalFunction.createSnackBar(layout, message, R.color.colorPrimary, "success")
    }

    override fun onFailed(error: String) {
        globalFunction.createSnackBar(layout, error, R.color.red, "error")
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