package com.mfa.carikerjapariwisata.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.model.Jobs
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_jobs.view.*
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class JobAdapter internal constructor(private val jobs: List<Jobs>, private val activity: String) :
    RecyclerView.Adapter<JobAdapter.ViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null
    private var filteredJobList = jobs

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
        if(activity == "JobFragment"){
            if(filteredJobList.count() < 5){
                return filteredJobList.count()
            }else{
                return 5
            }
        }else{
            return filteredJobList.count()
        }
    }

    fun getFilter(): Filter? {
        return object : Filter() {
             override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val searchString = charSequence.toString()
                if (searchString.isEmpty()) {
                    filteredJobList = jobs
                } else {
                    val tempFilteredList: ArrayList<Jobs> = ArrayList()
                    for (job in jobs) {

                        // search for user title
                        if (job.job_title?.toLowerCase()?.contains(searchString.toLowerCase())!! || job.job_place?.toLowerCase()?.contains(searchString.toLowerCase())!!) {
                            tempFilteredList.add(job)
                        }
                    }
                    filteredJobList = tempFilteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredJobList
                return filterResults
            }

            protected override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults
            ) {
                filteredJobList = filterResults.values as List<Jobs>
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredJobList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(jobs: Jobs) {
            with(itemView) {
                val calCurr: Calendar = Calendar.getInstance()
                val day: Calendar = Calendar.getInstance()
                day.setTime(SimpleDateFormat("yyyy-MM-dd").parse(jobs.job_date_end))
                val day_count = ((day.timeInMillis - calCurr.timeInMillis)/ (1000 * 60 * 60 * 24))
                if(day_count >= 0){
                    tvTitle.text = jobs.job_title
                    tvPlace.text = jobs.job_place
                    Picasso.with(context).load(ApiClient.IMAGE_URL+ (jobs.photo))
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .error(R.drawable.im_slider1)
                        .into(ivJobLogo)

                    tvTimeLeft.text = "Sisa "+(((day.timeInMillis - calCurr.timeInMillis)/ (1000 * 60 * 60 * 24)).toString())+ " hari"
                    tvTimeLeft.setTextColor(resources.getColor(R.color.colorPrimary))

                    itemView.setOnClickListener { onItemClickCallback?.onItemClicked(jobs) }
                }else{
                    layout.visibility = View.GONE
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Jobs)
    }
}