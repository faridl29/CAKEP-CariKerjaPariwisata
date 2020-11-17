package com.mfa.carikerjapariwisata.views.ui.job_detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.databinding.FragmentJobDetailBinding
import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.model.SharedPrefManager
import com.mfa.carikerjapariwisata.views.ui.apply_job.ApplyJobActivity
import kotlinx.android.synthetic.main.fragment_job_detail.*

class JobDetailFragment : SuperBottomSheetFragment() {

    private lateinit var jobDetailViewModel: JobDetailViewModel
    private lateinit var binding: FragmentJobDetailBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private var mBehavior: BottomSheetBehavior<View>? = null

    companion object {
        const val EXTRA_JOB = "extra_job"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        jobDetailViewModel = activity?.let { ViewModelProviders.of(it).get(JobDetailViewModel::class.java) }!!
        binding = FragmentJobDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        sharedPrefManager = SharedPrefManager(view.context)

        binding.btnApplyJob.setOnClickListener {
            val intent = Intent(activity, ApplyJobActivity::class.java)
            startActivity(intent)
        }


        binding.btClose.setOnClickListener{
            dismiss()
        }

        binding.viewModel = jobDetailViewModel
        val job = arguments?.getParcelable<Jobs>(EXTRA_JOB)

        if (job != null) {
            jobDetailViewModel.setData(job)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val job = arguments?.getParcelable<Jobs>(EXTRA_JOB)
        if(job?.user_id == sharedPrefManager.spId){
            btnApplyJob.setEnabled(false)
            btnApplyJob.setBackgroundColor(ContextCompat.getColor(view.context, R.color.grey_20))
        }
    }

}