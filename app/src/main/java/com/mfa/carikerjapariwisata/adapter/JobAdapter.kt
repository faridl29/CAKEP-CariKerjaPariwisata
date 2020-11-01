package com.mfa.carikerjapariwisata.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.model.Jobs
import kotlinx.android.synthetic.main.list_jobs.view.*
import java.text.SimpleDateFormat
import java.util.*

class JobAdapter internal constructor(private val jobs: List<Jobs>) :
    RecyclerView.Adapter<JobAdapter.ViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_jobs, parent, false)
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
                tvTitle.text = jobs.job_title
                tvPlace.text = jobs.job_place

                val calCurr: Calendar = Calendar.getInstance()
                val day: Calendar = Calendar.getInstance()
                day.setTime(SimpleDateFormat("yyyy-MM-dd").parse(jobs.job_date_end))
                tvTimeLeft.text = "Sisa "+(((day.timeInMillis - calCurr.timeInMillis)/ (1000 * 60 * 60 * 24)).toString())+ " hari"

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(jobs) }

            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Jobs)
    }
}