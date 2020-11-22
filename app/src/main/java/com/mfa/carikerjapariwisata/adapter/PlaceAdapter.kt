package com.mfa.carikerjapariwisata.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.model.Place
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_places.view.*
import kotlinx.android.synthetic.main.list_places.view.tvTitle


class PlaceAdapter internal constructor(private val places: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    private var onItemClickCallback: PlaceAdapter.OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_places, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return places.count()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position == places.lastIndex ,places[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bind(lastItem: Boolean, place: Place) {
            with(itemView) {
                tvTitle.text = place.place_name
                tvCity.text = place.place_city
                Picasso.with(context).load(ApiClient.IMAGE_URL+ (place.photo_main))
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.im_slider1)
                    .into(ivPhoto)

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(place) }
                if(lastItem){
//                    val params = ActionMenuView.LayoutParams(
//                        ActionMenuView.LayoutParams.WRAP_CONTENT,
//                        ActionMenuView.LayoutParams.WRAP_CONTENT
//                    )
//
//                    cardView.layoutParams = params
//                    val params2 = FrameLayout.LayoutParams(
//                        ActionMenuView.LayoutParams.WRAP_CONTENT,
//                        ActionMenuView.LayoutParams.WRAP_CONTENT
//                    )
//                    params2.marginEnd = resources.getDimensionPixelSize(R.dimen._minus10sdp)
//                    layout.layoutParams = params2
                }

            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Place)
    }
}