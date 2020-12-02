package com.mfa.carikerjapariwisata.views.ui.applicant

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.adapter.ApplicantAdapter
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.model.Applicants
import com.mfa.carikerjapariwisata.model.Attachments
import com.mfa.carikerjapariwisata.utils.DownloadTask
import com.mfa.carikerjapariwisata.utils.GlobalFunction
import kotlinx.android.synthetic.main.activity_all_job.*
import kotlinx.android.synthetic.main.activity_applicant.*
import kotlinx.android.synthetic.main.activity_applicant.bt_close
import kotlinx.android.synthetic.main.activity_applicant.layout
import kotlinx.android.synthetic.main.activity_applicant.lytListEmpty


class ApplicantActivity : AppCompatActivity(), ApplicantView {
    private lateinit var presenter: ApplicantPresenter
    private lateinit var globalFunction: GlobalFunction
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applicant)

        presenter = ApplicantPresenter(this)
        onAttachView()

        globalFunction = GlobalFunction(this)

        bt_close.setOnClickListener {
            finish()
        }
    }

    override fun onSuccess(result: List<Applicants>) {
        val adapter = ApplicantAdapter(result)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rvApplicant.apply {
            this.adapter = adapter
            this.layoutManager = layoutManager
            this.hasFixedSize()
        }

        verifyStoragePermissions(this)

        adapter.setOnItemClickCallbackAttachment(object : ApplicantAdapter.OnItemClickCallbackAttachment {
            override fun onItemClickedAttachment(data: Attachments) {
                downloadFile(ApiClient.ATTACHMENT_URL+data.attachment)
            }

        })
    }

    private fun downloadFile(fileUrl: String){
        DownloadTask(this, fileUrl)
    }

    fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have write permission
        val permission =
            ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }
    override fun onFailed(error: String) {
        globalFunction.createSnackBar(layout, error, R.color.red, "error")
    }

    override fun onEmpty() {
        lytListEmpty.visibility = View.VISIBLE
    }

    override fun onAttachView() {
        presenter.onAttach(this)
        presenter.getApplicant(intent.getStringExtra("job_id"))
    }

    override fun onDetachView() {
        presenter.onDetach()
    }

    override fun onDestroy() {
        onDetachView()
        super.onDestroy()
    }
}