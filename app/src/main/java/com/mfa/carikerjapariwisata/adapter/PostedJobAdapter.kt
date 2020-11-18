package com.mfa.carikerjapariwisata.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.model.Jobs
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_jobs.view.*
import kotlinx.android.synthetic.main.list_jobs.view.ivJobLogo
import kotlinx.android.synthetic.main.list_jobs.view.tvPlace
import kotlinx.android.synthetic.main.list_jobs.view.tvTimeLeft
import kotlinx.android.synthetic.main.list_jobs.view.tvTitle
import kotlinx.android.synthetic.main.list_main_places.view.*
import kotlinx.android.synthetic.main.list_posted_job.view.*
import java.text.SimpleDateFormat
import java.util.*

class PostedJobAdapter internal constructor(private val jobs: List<Jobs>) :
    RecyclerView.Adapter<PostedJobAdapter.ViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_posted_job, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return jobs.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(jobs[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(jobs: Jobs) {
            with(itemView) {
                val calCurr: Calendar = Calendar.getInstance()
                val day: Calendar = Calendar.getInstance()
                day.setTime(SimpleDateFormat("yyyy-MM-dd").parse(jobs.job_date_end))
                val day_count = ((day.timeInMillis - calCurr.timeInMillis)/ (1000 * 60 * 60 * 24))

                tvTitle.text = jobs.job_title
                tvPlace.text = jobs.job_place
                Picasso.with(context).load(ApiClient.IMAGE_URL+ (jobs.photo))
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.im_slider1)
                    .into(ivJobLogo)

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(jobs) }

                if(day_count >= 0){
                    tvTimeLeft.text = "Sisa "+(((day.timeInMillis - calCurr.timeInMillis)/ (1000 * 60 * 60 * 24)).toString())+ " hari"
                    tvTimeLeft.setTextColor(resources.getColor(R.color.colorPrimary))
                }else{
                    tvTimeLeft.text = "Kadaluarsa"
                    tvTimeLeft.setTextColor(resources.getColor(R.color.red))
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Jobs)
    }
}