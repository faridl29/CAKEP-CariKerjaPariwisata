package com.mfa.carikerjapariwisata.adapter

import android.R.attr
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ActionMenuView
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.model.Place
import kotlinx.android.synthetic.main.list_places.view.*


class PlaceAdapter internal constructor(private val places: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bind(lastItem: Boolean, place: Place) {
            with(itemView) {
                tvTitle.text = place.place_name
                tvCity.text = place.place_city
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

}