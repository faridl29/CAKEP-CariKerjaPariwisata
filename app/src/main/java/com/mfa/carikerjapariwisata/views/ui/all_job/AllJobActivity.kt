package com.mfa.carikerjapariwisata.views.ui.all_job

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.adapter.JobAdapter
import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.utils.GlobalFunction
import com.mfa.carikerjapariwisata.views.ui.job_detail.JobDetailFragment
import kotlinx.android.synthetic.main.activity_all_job.*
import kotlinx.android.synthetic.main.activity_posted_job.layout


class AllJobActivity : AppCompatActivity(), AllJobView {
    private lateinit var presenter: AllJobPresenter
    private lateinit var globalFunction: GlobalFunction
    private lateinit var jobAdapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_job)

        globalFunction = GlobalFunction(this)

        presenter = AllJobPresenter(this)
        onAttachView()

//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener() {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                jobAdapter.getFilter()?.filter(newText)
//                return true
//            }
//        })

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
                jobAdapter.getFilter()?.filter(s)
                if (s.toString().trim { it <= ' ' }.length == 0) {
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

        jobAdapter.setOnItemClickCallback(object : JobAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Jobs) {
                val sheet = JobDetailFragment()
                var args = Bundle()
                args.putParcelable(JobDetailFragment.EXTRA_JOB, data)
                sheet.setArguments(args)
                supportFragmentManager?.let { it1 -> sheet.show(it1, "JobDetailFragment") }
            }
        })

        presenter.onCleared()
    }

    override fun onFailed(error: String) {
        globalFunction.createSnackBar(layout, error, R.color.red)
        presenter.onCleared()
    }

    override fun onEmpty() {
        TODO("Not yet implemented")
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
}