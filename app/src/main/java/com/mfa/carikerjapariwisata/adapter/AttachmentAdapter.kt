package com.mfa.carikerjapariwisata.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.model.Attachments
import kotlinx.android.synthetic.main.list_attachments.view.*

class AttachmentAdapter internal constructor(private val attachments: List<Attachments>) :
    RecyclerView.Adapter<AttachmentAdapter.ViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_attachments, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return attachments.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(attachments[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(attachment: Attachments) {
            with(itemView) {

                tvAttachment.text = attachment.attachment
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(attachment) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Attachments)
    }
}