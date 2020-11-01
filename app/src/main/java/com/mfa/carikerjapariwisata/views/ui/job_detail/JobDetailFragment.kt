package com.mfa.carikerjapariwisata.views.ui.job_detail

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mfa.carikerjapariwisata.databinding.FragmentJobDetailBinding
import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.views.ui.apply_job.ApplyJobActivity
import kotlinx.android.synthetic.main.fragment_job_detail.*

class JobDetailFragment : SuperBottomSheetFragment() {

    private lateinit var jobDetailViewModel: JobDetailViewModel
    private lateinit var binding: FragmentJobDetailBinding
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

        binding.btnApplyJob.setOnClickListener {
            val intent = Intent(activity, ApplyJobActivity::class.java)
            startActivity(intent)
        }

//        mBehavior =  BottomSheetBehavior.from(binding.lytParent.rootView)
//        mBehavior!!.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
//
//        mBehavior!!.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                if (BottomSheetBehavior.STATE_EXPANDED === newState) {
//                    showView(app_bar_layout, getActionBarSize())
//                    hideView(lyt_profile)
//                }
//                if (BottomSheetBehavior.STATE_COLLAPSED === newState) {
//                    hideView(app_bar_layout)
//                    showView(lyt_profile, getActionBarSize())
//                }
//
//                if (BottomSheetBehavior.STATE_HIDDEN === newState) {
//                    dismiss()
//                }
//            }
//
//        })

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

//    override fun onStart() {
//        super.onStart()
//        mBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
//    }
//
//    private fun hideView(view: View) {
//        val params = view.layoutParams
//        params.height = 0
//        view.layoutParams = params
//    }
//
//    private fun showView(view: View, size: Int) {
//        val params = view.layoutParams
//        params.height = size
//        view.layoutParams = params
//    }
//
//    private fun getActionBarSize(): Int {
//        val styledAttributes =
//            context!!.theme.obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
//        return styledAttributes.getDimension(0, 0f).toInt()
//    }
}