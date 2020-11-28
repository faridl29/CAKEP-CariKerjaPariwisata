package com.mfa.carikerjapariwisata.adapter

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.model.Place
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_jobs.view.*
import kotlinx.android.synthetic.main.list_places.view.*
import kotlinx.android.synthetic.main.list_places.view.tvTitle


class PlaceAdapter internal constructor(private var places: List<Place>, private val activity: String) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    private var onItemClickCallback: PlaceAdapter.OnItemClickCallback? = null
    private var onItemClickCallbackLike: PlaceAdapter.OnItemClickCallbackLike? = null
    private var filteredPlaceList = places

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setOnItemClickCallbackLike(onItemClickCallbackLike: OnItemClickCallbackLike) {
        this.onItemClickCallbackLike = onItemClickCallbackLike
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(activity == "HomeFragment"){
            return ViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.list_places, parent, false)
            )
        } else{
            return ViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.list_all_place, parent, false)
            )
        }

    }

    override fun getItemCount(): Int {
        if(activity == "HomeFragment"){
            if(filteredPlaceList.count() < 5){
                return filteredPlaceList.count()
            }else{
                return 5
            }
        }else{
            return filteredPlaceList.count()
        }
    }

    fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val searchString = charSequence.toString()
                if (searchString.isEmpty()) {
                    filteredPlaceList = places
                } else {
                    val tempFilteredList: ArrayList<Place> = ArrayList()
                    for (place in places) {

                        // search for user title
                        if (place.place_name?.toLowerCase()?.contains(searchString.toLowerCase())!! || place.place_city?.toLowerCase()?.contains(searchString.toLowerCase())!!) {
                            tempFilteredList.add(place)
                        }
                    }
                    filteredPlaceList = tempFilteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredPlaceList
                return filterResults
            }

            protected override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults
            ) {
                filteredPlaceList = filterResults.values as List<Place>
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position,filteredPlaceList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bind(position: Int, place: Place) {
            with(itemView) {
                tvTitle.text = place.place_name
                tvCity.text = place.place_city
                tvLike.text = place.like_count
                Picasso.with(context).load(ApiClient.IMAGE_URL+ (place.photo_main))
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.im_slider1)
                    .into(ivPhoto)

                fabLike.setOnClickListener {
                    onItemClickCallbackLike?.onItemClicked(place, position)
                }

                if(place.like == true){
                    fabLike.setImageResource(R.drawable.ic_love_active)
                }else{
                    fabLike.setImageResource(R.drawable.ic_love_not_active)
                }

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(place) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Place)
    }

    interface OnItemClickCallbackLike {
        fun onItemClicked(data: Place, position: Int)
    }

    fun like_clicked(position: Int, like: Boolean?){
        places[position].like = like
        notifyItemChanged(position)
    }
}