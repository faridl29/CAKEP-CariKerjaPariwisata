package com.mfa.carikerjapariwisata.views.ui.bookmarked_job

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.adapter.JobAdapter
import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.utils.GlobalFunction
import com.mfa.carikerjapariwisata.views.ui.job_detail.JobDetailFragment
import kotlinx.android.synthetic.main.activity_bookmarked_job.*

class BookmarkedJobActivity : AppCompatActivity(), BookmarkedJobView {
    private lateinit var presenter: BookmarkedJobPresenter
    private lateinit var globalFunction: GlobalFunction
    private var jobAdapter: JobAdapter? = null
    private var index: Int = 0
    private var RESULT_CODE = 0
    companion object{
        const val ACTIVITY_TYPE = "activity_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmarked_job)

        globalFunction = GlobalFunction(this)

        presenter = BookmarkedJobPresenter(this)
        onAttachView()

    }

    override fun onSuccess(result: List<Jobs>) {
        jobAdapter = JobAdapter(result, "AllJobActivity")
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rvJobs.apply {
            this.adapter = jobAdapter
            this.layoutManager = layoutManager
            this.hasFixedSize()
        }

        jobAdapter?.setOnItemClickCallback(object : JobAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Jobs) {
                val sheet = JobDetailFragment()
                var args = Bundle()
                args.putParcelable(JobDetailFragment.EXTRA_JOB, data)
                args.putString(JobDetailFragment.ACTIVITY_TYPE, "BookmarkedJobActivity")
                sheet.setArguments(args)
                supportFragmentManager?.let { it1 -> sheet.show(it1, "JobDetailFragment") }
            }
        })

        jobAdapter?.setOnItemClickCallbackBookmark(object : JobAdapter.OnItemClickCallbackBookmark{
            override fun onItemClicked(
                data: Jobs,
                position: Int
            ) {
                presenter.bookmark_job(data.id)
                index = position
            }

        })

        jobAdapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                if(jobAdapter?.itemCount!! > 0){
                    rvJobs.visibility = View.VISIBLE
                    lytSearhEmpty.visibility = View.GONE
                }else{
                    rvJobs.visibility = View.GONE
                    lytSearhEmpty.visibility = View.VISIBLE
                }
            }
        })

    }

    fun refresh_data(resultCode: Int){
        if(resultCode == Activity.RESULT_OK){
            RESULT_CODE = Activity.RESULT_OK
            presenter.getBookmarkedJob()
        }
    }

    override fun onFailed(error: String) {
        globalFunction.createSnackBar(layout, error, R.color.red,"error")
    }

    override fun onEmpty() {
        rvJobs.visibility = View.GONE
        lytListEmpty.visibility = View.VISIBLE
    }

    override fun onSuccessBookmarkJob(status: Boolean) {
        RESULT_CODE = Activity.RESULT_OK
        presenter.getBookmarkedJob()
    }

    override fun onFailedBookmarkJob(error: String) {
        jobAdapter?.notifyDataSetChanged()
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun onAttachView() {
        presenter.onAttach(this)
        presenter.getBookmarkedJob()
    }

    override fun onDetachView() {
        presenter.onDetach()
    }

    override fun onDestroy() {
        onDetachView()
        super.onDestroy()
    }

    override fun onBackPressed() {
        var intent = Intent()
        setResult(RESULT_CODE, intent)
        finish()
        super.onBackPressed()
    }
}