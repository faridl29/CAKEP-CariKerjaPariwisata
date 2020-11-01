package com.mfa.carikerjapariwisata.views.ui.job_detail

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.mfa.carikerjapariwisata.model.Jobs
import kotlinx.android.synthetic.main.list_jobs.view.*
import java.text.SimpleDateFormat
import java.util.*

class JobDetailViewModel : ViewModel() {
    var title: ObservableField<String> = ObservableField()
    var place: ObservableField<String> = ObservableField()
    var dateEnd: ObservableField<String> = ObservableField()
    var sallary: ObservableField<String> = ObservableField()
    var city: ObservableField<String> = ObservableField()
    var desc: ObservableField<String> = ObservableField()


    fun setData(job: Jobs) {
        title.set(job.job_title)
        place.set(job.job_place)
        sallary.set(job.job_sallary)
        city.set(job.job_city)
        desc.set(job.job_desc)

        val calCurr: Calendar = Calendar.getInstance()
        val day: Calendar = Calendar.getInstance()
        day.setTime(SimpleDateFormat("yyyy-MM-dd").parse(job.job_date_end))
        dateEnd.set("Sisa "+(((day.timeInMillis - calCurr.timeInMillis)/ (1000 * 60 * 60 * 24)).toString())+ " hari")

    }
}