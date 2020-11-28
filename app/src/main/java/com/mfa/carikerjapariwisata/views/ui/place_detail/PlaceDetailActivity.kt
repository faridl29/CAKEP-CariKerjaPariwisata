package com.mfa.carikerjapariwisata.views.ui.place_detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.databinding.ActivityPlaceDetailBinding
import com.mfa.carikerjapariwisata.model.Place
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_place_detail.*


class PlaceDetailActivity : AppCompatActivity(), PlaceDetailView {

    private lateinit var binding: ActivityPlaceDetailBinding
    private lateinit var viewModel: PlaceDetailViewModel
    private lateinit var presenter: PlaceDetailPresenter
    private lateinit var place: Place
    private var RESULT_CODE = 0

    companion object {
        const val EXTRA_PLACE = "extra_place"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        presenter = PlaceDetailPresenter(this)
        onAttachView()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_detail)
//        viewModel = ViewModelProviders.of(this).get(PlaceDetailViewModel::class.java)

//        binding.viewModel = viewModel

        place = intent.getParcelableExtra<Place>(EXTRA_PLACE)
//        viewModel.setData(place)

        binding.tvTitle.text = place?.place_name
        binding.tvCity.text = place?.place_city
        binding.tvDesc.text = place?.place_desc
        binding.tvPrice.text = place?.place_price
        binding.tvTime.text = place?.place_time
        binding.tvLikes.text = place?.like_count

        Picasso.with(this).load(ApiClient.IMAGE_URL+ (place.photo_main))
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .error(R.drawable.im_slider1)
            .into(binding.ivMainPhoto)

        for ((index, value) in place.galleries!!.withIndex()){
            if(index == 0){
                Picasso.with(this).load(ApiClient.IMAGE_URL+ (value.gallery_name))
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.im_slider1)
                    .into(binding.ivChild1)
            }else if(index == 1){
                Picasso.with(this).load(ApiClient.IMAGE_URL+ (value.gallery_name))
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.im_slider1)
                    .into(binding.ivChild2)
            }else if(index == 2){
                Picasso.with(this).load(ApiClient.IMAGE_URL+ (value.gallery_name))
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.im_slider1)
                    .into(binding.ivChild3)
            }

        }

        if(place.like == true){
            binding.fabLike.setImageDrawable(resources.getDrawable(R.drawable.ic_love_active))
        }else{
            binding.fabLike.setImageDrawable(resources.getDrawable(R.drawable.ic_love_not_active))
        }

        fabLike.setOnClickListener {
            presenter.like_place(place?.id)
        }

    }

    override fun onSuccessLikePlace(status: Boolean) {
        RESULT_CODE = Activity.RESULT_OK
        place?.like = status
        if(status == true){
            binding.fabLike.setImageDrawable(resources.getDrawable(R.drawable.ic_love_active))
        }else{
            binding.fabLike.setImageDrawable(resources.getDrawable(R.drawable.ic_love_not_active))
        }
    }

    override fun onFailedLikePlace(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun onAttachView() {
        presenter.onAttach(this)
    }

    override fun onDetachView() {
        presenter.onDetach()
    }

    override fun onDestroy() {
        onDetachView()
        super.onDestroy()
    }

    override fun onBackPressed() {
        var intent =  Intent()
        setResult(RESULT_CODE, intent)
        finish()
        super.onBackPressed()
    }
}