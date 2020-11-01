package com.mfa.carikerjapariwisata.views.ui.place_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.databinding.ActivityPlaceDetailBinding
import com.mfa.carikerjapariwisata.model.Place
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_main_places.view.*
import java.util.EnumSet.of

class PlaceDetail : AppCompatActivity() {

    private lateinit var binding: ActivityPlaceDetailBinding
    private lateinit var viewModel: PlaceDetailViewModel

    companion object {
        const val EXTRA_PLACE = "extra_place"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_detail)
        viewModel = ViewModelProviders.of(this).get(PlaceDetailViewModel::class.java)

        binding.viewModel = viewModel

        val place = intent.getParcelableExtra<Place>(EXTRA_PLACE)
        viewModel.setData(place)

        Picasso.with(this).load(ApiClient.IMAGE_URL+ (place.photo_main))
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .error(R.drawable.im_slider1)
            .into(binding.ivMainPhoto)

        for ((index, value) in place.galleries!!.withIndex()){
            var photo: ImageView? = null
            if(index == 0){
                photo = binding.ivChild1
            }else if(index == 1){
                photo = binding.ivChild2
            }else if(index == 2){
                photo = binding.ivChild3
            }
            Picasso.with(this).load(ApiClient.IMAGE_URL+ (value.gallery_name))
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .error(R.drawable.im_slider1)
                .into(photo)
        }

    }
}