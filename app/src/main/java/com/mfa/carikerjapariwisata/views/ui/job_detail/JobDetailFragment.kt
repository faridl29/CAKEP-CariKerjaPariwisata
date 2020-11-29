package com.mfa.carikerjapariwisata.views.ui.job_detail

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.databinding.FragmentJobDetailBinding
import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.model.SharedPrefManager
import com.mfa.carikerjapariwisata.views.ui.all_job.AllJobActivity
import com.mfa.carikerjapariwisata.views.ui.applicant.ApplicantActivity
import com.mfa.carikerjapariwisata.views.ui.apply_job.ApplyJobActivity
import com.mfa.carikerjapariwisata.views.ui.bookmarked_job.BookmarkedJobActivity
import com.mfa.carikerjapariwisata.views.ui.job.JobFragment
import com.mfa.carikerjapariwisata.views.ui.posted_job.PostedJobActivity
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_job_detail.*
import java.text.SimpleDateFormat
import java.util.*


class JobDetailFragment : SuperBottomSheetFragment(), JobDetailView {

//    private lateinit var jobDetailViewModel: JobDetailViewModel
    private lateinit var presenter: JobDetailPresenter
    private lateinit var binding: FragmentJobDetailBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private var mBehavior: BottomSheetBehavior<View>? = null
    private lateinit var job: Jobs
    private lateinit var activity_type: String
    private var RESULT_CODE = 0

    companion object {
        const val EXTRA_JOB = "extra_job"
        const val ACTIVITY_TYPE = "activity_type"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentJobDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        presenter = JobDetailPresenter(view.context)
        onAttachView()
        sharedPrefManager = SharedPrefManager(view.context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        job = arguments?.getParcelable<Jobs>(EXTRA_JOB)!!
        activity_type = arguments?.getString(ACTIVITY_TYPE).toString()

        val calCurr: Calendar = Calendar.getInstance()
        val day: Calendar = Calendar.getInstance()
        day.setTime(SimpleDateFormat("yyyy-MM-dd").parse(job?.job_date_end))

        binding.tvTitle.text = job.job_title
        binding.tvPlace.text = job.job_place
        binding.tvTime.text = "Sisa "+(((day.timeInMillis - calCurr.timeInMillis)/ (1000 * 60 * 60 * 24)).toString())+ " hari"
        binding.tvJobDesk.text = job.job_desc
        binding.tvGaji.text = job.job_sallary
        binding.tvCreatedAt.text = job.created_at

        if(job.bookmark == true){
            binding.ivBookmark.setImageDrawable(resources.getDrawable(R.drawable.ic_bookmark_active))
        }else{
            binding.ivBookmark.setImageDrawable(resources.getDrawable(R.drawable.ic_bookmark_not_active))
        }

        Picasso.with(context).load(ApiClient.JOB_URL+ (job?.photo))
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .error(R.drawable.im_slider1)
            .into(ivJobLogo)

        if(job.user_id == sharedPrefManager.spId){
            binding.btnApplyJob.text = "Lihat Pelamar"
        }

        binding.btnApplyJob.setOnClickListener {
            if(job.user_id == sharedPrefManager.spId){
                val intent = Intent(activity, ApplicantActivity::class.java)
                intent.putExtra("job_id", job.id)
                startActivity(intent)
            }else{
                val intent = Intent(activity, ApplyJobActivity::class.java)
                intent.putExtra("job_id", job.id)
                startActivity(intent)
            }
        }

        binding.ivBookmark.setOnClickListener {
            presenter.bookmark_job(job.id)
        }

        binding.btClose.setOnClickListener{
            dismiss()
        }
    }

    override fun onSuccessBookmarkJob(status: Boolean) {
        job.bookmark = status
        RESULT_CODE = RESULT_OK
        if(status == true){
            binding.ivBookmark.setImageDrawable(resources.getDrawable(R.drawable.ic_bookmark_active))
        }else{
            binding.ivBookmark.setImageDrawable(resources.getDrawable(R.drawable.ic_bookmark_not_active))
        }
    }

    override fun onFailedBookmarkJob(error: String) {
        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
    }

    override fun onAttachView() {
        presenter.onAttach(this)
    }

    override fun onDetachView() {
        presenter.onDetach()
    }

    override fun onDestroy() {
        onDetachView()
        super.onDestroy()
    }

    override fun onDismiss(dialog: DialogInterface) {
        if(activity_type == "JobFragment"){
            var fragment: JobFragment = activity?.supportFragmentManager?.findFragmentByTag("2") as JobFragment
            fragment.refresh_data(RESULT_CODE)
        }else if (activity_type == "AllJobActivity"){
            (activity as AllJobActivity?)?.refresh_data(RESULT_CODE)
        }else if (activity_type == "BookmarkedJobActivity"){
            (activity as BookmarkedJobActivity?)?.refresh_data(RESULT_CODE)
        }else if (activity_type == "PostedJobActivity"){
            (activity as PostedJobActivity?)?.refresh_data(RESULT_CODE)
        }
        super.onDismiss(dialog)
    }

}