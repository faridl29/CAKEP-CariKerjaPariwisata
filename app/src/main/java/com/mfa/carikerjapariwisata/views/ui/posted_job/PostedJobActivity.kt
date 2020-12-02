package com.mfa.carikerjapariwisata.views.ui.posted_job

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.adapter.PostedJobAdapter
import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.utils.GlobalFunction
import com.mfa.carikerjapariwisata.views.ui.edit_job.EditJobActivity
import com.mfa.carikerjapariwisata.views.ui.job_detail.JobDetailFragment
import kotlinx.android.synthetic.main.activity_all_job.*
import kotlinx.android.synthetic.main.activity_posted_job.*
import kotlinx.android.synthetic.main.activity_posted_job.bt_close
import kotlinx.android.synthetic.main.activity_posted_job.layout

class PostedJobActivity : AppCompatActivity(), PostedJobView {
    private lateinit var presenter: PostedJobPresenter
    private lateinit var globalFunction: GlobalFunction
    private var REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posted_job)

        presenter = PostedJobPresenter(this)
        onAttachView()

        globalFunction = GlobalFunction(this)

        bt_close.setOnClickListener {
            finish()
        }
    }

    fun refresh_data(resultCode: Int){
        if(resultCode == Activity.RESULT_OK){
            presenter.getPostedJob()
        }
    }

    override fun onSuccess(result: List<Jobs>) {
        val postedJobAdapter = PostedJobAdapter(result)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rvPostedJob.apply {
            this.adapter = postedJobAdapter
            this.layoutManager = layoutManager
            this.hasFixedSize()
        }

        postedJobAdapter.setOnItemClickCallback(object : PostedJobAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Jobs, type: String) {
                if(type == "edit_job"){
                    val intent = Intent(this@PostedJobActivity, EditJobActivity::class.java)
                    intent.putExtra(EditJobActivity.EXTRA_JOB, data)
                    startActivityForResult(intent, REQUEST_CODE)
                } else if(type == "delete_job"){
                    SweetAlertDialog(this@PostedJobActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah anda yakin?")
                        .setConfirmButtonBackgroundColor(resources.getColor(R.color.colorPrimary))
                        .setContentText("Akan menghapus pekerjaan!")
                        .setConfirmText("Ya!")
                        .setConfirmClickListener {
                            sDialog -> sDialog.dismissWithAnimation()
                            presenter.deleteJob(data.id) }
                        .setCancelButton(
                            "Batal"
                        ) { sDialog -> sDialog.dismissWithAnimation() }
                        .show()
                } else{
                    val sheet = JobDetailFragment()
                    val args = Bundle()
                    args.putParcelable(JobDetailFragment.EXTRA_JOB, data)
                    args.putString(JobDetailFragment.ACTIVITY_TYPE, "PostedJobActivity")
                    sheet.setArguments(args)
                    supportFragmentManager?.let { it1 -> sheet.show(it1, "JobDetailFragment") }
                }
            }
        })
    }

    override fun onFailed(error: String) {
        globalFunction.createSnackBar(layout, error, R.color.red, "error")
    }

    override fun onEmpty() {
        Toast.makeText(this, "data empty", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessDeleteJob() {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Yeay!")
            .setContentText("Pekerjaan berhasil dihapus!")
            .show()
        presenter.getPostedJob()
    }

    override fun onFailedDeleteJob() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Oops...")
            .setContentText("Pekerjaan gagal dihapus!")
            .show()
    }

    override fun onAttachView() {
        presenter.onAttach(this)
        presenter.getPostedJob()
    }

    override fun onDetachView() {
        presenter.onDetach()
    }

    override fun onDestroy() {
        onDetachView()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Yeay!")
                    .setContentText("Pekerjaan berhasil diupdate!")
                    .show()
            }
        }
    }
}