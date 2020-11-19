package com.mfa.carikerjapariwisata.views.ui.job

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.adapter.JobAdapter
import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.views.ui.all_job.AllJobActivity
import com.mfa.carikerjapariwisata.views.ui.create_job.CreateJobActivity
import com.mfa.carikerjapariwisata.views.ui.job_detail.JobDetailFragment
import com.mfa.carikerjapariwisata.views.ui.place_detail.PlaceDetail
import kotlinx.android.synthetic.main.fragment_job.*

class JobFragment : Fragment() {

    private lateinit var jobViewModel: JobViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        jobViewModel =
                ViewModelProviders.of(this).get(JobViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_job, container, false)

        jobViewModel.data.observe({ lifecycle }, {
            val jobAdapter = JobAdapter(it, "JobFragment")

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
                    sheet.setArguments(args)
                    fragmentManager?.let { it1 -> sheet.show(it1, "JobDetailFragment") }
                }
            })

            btCreateJob.setOnClickListener {
                val intent = Intent(activity, CreateJobActivity::class.java)
                startActivity(intent)
            }

        })

//        val data = jobViewModel.response.observe({ lifecycle }, {
//            if (it.isNotEmpty()) {
//                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
//            }
//        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivLoadAll.setOnClickListener {
            val intent = Intent(activity, AllJobActivity::class.java)
            startActivity(intent)
        }
    }
}