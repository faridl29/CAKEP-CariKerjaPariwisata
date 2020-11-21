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
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.databinding.FragmentJobDetailBinding
import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.model.SharedPrefManager
import com.mfa.carikerjapariwisata.views.ui.apply_job.ApplyJobActivity
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_job_detail.*
import kotlinx.android.synthetic.main.list_main_places.view.*

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

        Picasso.with(context).load(ApiClient.IMAGE_URL+ (job?.photo))
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .error(R.drawable.im_slider1)
            .into(ivJobLogo)

        if(job?.user_id == sharedPrefManager.spId){
            btnApplyJob.setEnabled(false)
            btnApplyJob.setBackgroundColor(ContextCompat.getColor(view.context, R.color.grey_20))
        }

        binding.btnApplyJob.setOnClickListener {
            val intent = Intent(activity, ApplyJobActivity::class.java)
            intent.putExtra("job_id", job?.id)
            startActivity(intent)
        }
    }

}