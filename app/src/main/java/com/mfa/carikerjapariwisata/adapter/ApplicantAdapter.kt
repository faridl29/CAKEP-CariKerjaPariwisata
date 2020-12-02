package com.mfa.carikerjapariwisata.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.model.Applicants
import com.mfa.carikerjapariwisata.model.Attachments
import com.mfa.carikerjapariwisata.utils.GridSpacingItemDecoration
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_applicant.view.*


class ApplicantAdapter internal constructor(private val applicants: List<Applicants>) :
    RecyclerView.Adapter<ApplicantAdapter.ViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null
    private var onItemClickCallbackAttachment: OnItemClickCallbackAttachment? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setOnItemClickCallbackAttachment(onItemClickCallbackAttachment: OnItemClickCallbackAttachment) {
        this.onItemClickCallbackAttachment = onItemClickCallbackAttachment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_applicant, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return applicants.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(applicants[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(applicant: Applicants) {
            with(itemView) {

                tvName.text = applicant.name
                tvCreatedAt.text = applicant.created_at
                tvDesc.text = applicant.applicant_desc
                Picasso.get().load(ApiClient.IMAGE_URL+ (applicant.photo))
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.im_slider1)
                    .into(ivPhoto)

                val adapter = AttachmentAdapter(applicant.attachments)
                val layoutManager = GridLayoutManager(context, 2)

                val spanCount = 2; // 2 columns
                val spacing = 20
                val includeEdge = false

                rvAttachment.apply {
                    this.adapter = adapter
                    this.layoutManager = layoutManager
                    this.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
                    this.hasFixedSize()
                }

                adapter.setOnItemClickCallback(object : AttachmentAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Attachments) {
                        onItemClickCallbackAttachment?.onItemClickedAttachment(data)
                    }

                })

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(applicant) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Applicants)
    }

    interface  OnItemClickCallbackAttachment {
        fun onItemClickedAttachment(data: Attachments)
    }

}