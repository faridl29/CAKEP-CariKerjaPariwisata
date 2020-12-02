package com.mfa.carikerjapariwisata.views.ui.job

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.adapter.JobAdapter
import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.utils.GlobalFunction
import com.mfa.carikerjapariwisata.views.ui.all_job.AllJobActivity
import com.mfa.carikerjapariwisata.views.ui.bookmarked_job.BookmarkedJobActivity
import com.mfa.carikerjapariwisata.views.ui.create_job.CreateJobActivity
import com.mfa.carikerjapariwisata.views.ui.job_detail.JobDetailFragment
import kotlinx.android.synthetic.main.fragment_job.*


class JobFragment : Fragment(), JobView {

    private lateinit var root: View
    private lateinit var presenter: JobPresenter
    private lateinit var globalFunction: GlobalFunction
    private lateinit var jobAdapter: JobAdapter
    private var index: Int = 0
    private var REQUEST_CODE = 11
    private var REQUEST_CODE_CREATE = 12

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_job, container, false)

        presenter = JobPresenter(root.context)
        globalFunction = GlobalFunction(root.context)

        onAttachView()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivLoadAll.setOnClickListener {
            val intent = Intent(activity, AllJobActivity::class.java)
            intent.putExtra(AllJobActivity.ACTIVITY_TYPE, "all_job")
            startActivityForResult(intent, REQUEST_CODE)
        }

        etSearch.setOnClickListener {
            val intent = Intent(activity, AllJobActivity::class.java)
            intent.putExtra(AllJobActivity.ACTIVITY_TYPE, "search")
            startActivityForResult(intent, REQUEST_CODE)
        }

        btCreateJob.setOnClickListener {
            val intent = Intent(activity, CreateJobActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_CREATE)
        }

        ivBookmarkedJob.setOnClickListener {
            val intent = Intent(activity, BookmarkedJobActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onSuccess(result: List<Jobs>) {
        jobAdapter = JobAdapter(result, "JobFragment")

        rvJobs.apply {
            this.adapter = jobAdapter
            this.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            this.hasFixedSize()
        }

        jobAdapter.setOnItemClickCallback(object : JobAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Jobs) {
                val sheet = JobDetailFragment()
                var args = Bundle()
                args.putParcelable(JobDetailFragment.EXTRA_JOB, data)
                args.putString(JobDetailFragment.ACTIVITY_TYPE, "JobFragment")
                sheet.setArguments(args)
                fragmentManager?.let { it1 -> sheet.show(it1, "JobDetailFragment") }
            }
        })

        jobAdapter.setOnItemClickCallbackBookmark(object : JobAdapter.OnItemClickCallbackBookmark{
            override fun onItemClicked(
                data: Jobs,
                position: Int
            ) {
                presenter.bookmark_job(data.id)
                index = position
            }

        })
    }

    fun refresh_data(resultCode: Int){
        if(resultCode == RESULT_OK){
            presenter.get_job_list()
        }
    }

    override fun onFailed(error: String) {
        globalFunction.createSnackBar(layout, error, R.color.red)
    }

    override fun onEmpty() {
        TODO("Not yet implemented")
    }

    override fun onSuccessBookmarkJob(status: Boolean) {
//        var bookmark: Boolean? = !drawable?.equals(resources.getDrawable(R.drawable.ic_bookmark_active).constantState)!!
        jobAdapter.bookmark_clicked(index, status)
    }

    override fun onFailedBookmarkJob(error: String) {
        jobAdapter.notifyDataSetChanged()
        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
    }

    override fun onAttachView() {
        presenter.onAttach(this)
        presenter.get_job_list()
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
        if (requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                presenter.get_job_list()
            }
        }else if(requestCode == REQUEST_CODE_CREATE){
            if(resultCode == RESULT_OK){
                SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Yeay!")
                    .setContentText("Pekerjaan berhasil didaftarkan!")
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.get_job_list()
    }
}