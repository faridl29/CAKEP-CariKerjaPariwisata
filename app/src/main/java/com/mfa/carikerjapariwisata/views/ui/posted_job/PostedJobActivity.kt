package com.mfa.carikerjapariwisata.views.ui.posted_job

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.adapter.PostedJobAdapter
import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.utils.GlobalFunction
import kotlinx.android.synthetic.main.activity_posted_job.*

class PostedJobActivity : AppCompatActivity(), PostedJobView {
    private lateinit var presenter: PostedJobPresenter
    private lateinit var globalFunction: GlobalFunction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posted_job)

        presenter = PostedJobPresenter(this)
        onAttachView()

        globalFunction = GlobalFunction(this)
    }

    override fun onSuccess(result: List<Jobs>) {
        val postedJobAdapter = PostedJobAdapter(result)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rvPostedJob.apply {
            this.adapter = postedJobAdapter
            this.layoutManager = layoutManager
            this.hasFixedSize()
        }
    }

    override fun onFailed(error: String) {
        globalFunction.createSnackBar(layout, error, R.color.red)
    }

    override fun onEmpty() {
        Toast.makeText(this, "data empty", Toast.LENGTH_SHORT).show()
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
}