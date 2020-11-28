package com.mfa.carikerjapariwisata.views.ui.all_job

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.adapter.JobAdapter
import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.utils.GlobalFunction
import com.mfa.carikerjapariwisata.views.ui.job.JobFragment
import com.mfa.carikerjapariwisata.views.ui.job_detail.JobDetailFragment
import kotlinx.android.synthetic.main.activity_all_job.*
import kotlinx.android.synthetic.main.activity_all_job.iv_clear_text
import kotlinx.android.synthetic.main.activity_all_job.lytListEmpty
import kotlinx.android.synthetic.main.activity_all_job.lytSearhEmpty
import kotlinx.android.synthetic.main.activity_all_job.searchView
import kotlinx.android.synthetic.main.activity_all_place.*
import kotlinx.android.synthetic.main.activity_posted_job.layout


class AllJobActivity : AppCompatActivity(), AllJobView {
    private lateinit var presenter: AllJobPresenter
    private lateinit var globalFunction: GlobalFunction
    private var jobAdapter: JobAdapter? = null
    private var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_job)

        globalFunction = GlobalFunction(this)

        presenter = AllJobPresenter(this)
        onAttachView()

        searchView.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                return@OnEditorActionListener true
            }
            false
        })

        iv_clear_text.setOnClickListener {
            searchView.text = null
        }

        /*hide/show clear button in search view*/

        val searchViewTextWatcher: TextWatcher = object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                jobAdapter?.getFilter()?.filter(s)
                if (s.toString().trim { it <= ' ' }.isEmpty()) {
                    iv_clear_text.setVisibility(View.GONE)
                } else {
                    iv_clear_text.setVisibility(View.VISIBLE)
                }

                return
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        }

        /*hide/show clear button in search view*/
        searchView.addTextChangedListener(
            searchViewTextWatcher
        )

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
                    lytSearhEmpty.visibility = View.GONE
                }else{
                    lytSearhEmpty.visibility = View.VISIBLE
                }
            }
        })

    }

    override fun onFailed(error: String) {
        globalFunction.createSnackBar(layout, error, R.color.red,"error")
    }

    override fun onEmpty() {
        lytListEmpty.visibility = View.VISIBLE
    }

    override fun onSuccessBookmarkJob(status: Boolean) {
        jobAdapter?.bookmark_clicked(index, status)
    }

    override fun onFailedBookmarkJob(error: String) {
        jobAdapter?.notifyDataSetChanged()
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun onAttachView() {
        presenter.onAttach(this)
        presenter.getJobList()
    }

    override fun onDetachView() {
        presenter.onDetach()
    }

    override fun onDestroy() {
        onDetachView()
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
        var intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {
        var intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
        super.onBackPressed()
    }
}