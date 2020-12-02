package com.mfa.carikerjapariwisata.views.ui.place_detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.model.Place
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_place_detail.*


class PlaceDetailActivity : AppCompatActivity(), PlaceDetailView {

    private lateinit var viewModel: PlaceDetailViewModel
    private lateinit var presenter: PlaceDetailPresenter
    private lateinit var place: Place
    private var collapsingToolbar: CollapsingToolbarLayout? = null
    private var appBarLayout: AppBarLayout? = null
    private var collapsedMenu: Menu? = null
    private var appBarExpanded = true
    private var RESULT_CODE = 0

    companion object {
        const val EXTRA_PLACE = "extra_place"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        presenter = PlaceDetailPresenter(this)
        onAttachView()
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_detail)
//        viewModel = ViewModelProviders.of(this).get(PlaceDetailViewModel::class.java)

//        binding.viewModel = viewModel

        place = intent.getParcelableExtra<Place>(EXTRA_PLACE)
//        viewModel.setData(place)

        tvTitle.text = place?.place_name
        tvCity.text = place?.place_city
        tvDesc.text = place?.place_desc
        tvPrice.text = place?.place_price
        tvTime.text = place?.place_time
        tvLikes.text = place?.like_count

        Picasso.get().load(ApiClient.IMAGE_URL+ (place.photo_main))
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .error(R.drawable.im_slider1)
            .into(ivMainPhoto)

        for ((index, value) in place.galleries!!.withIndex()){
            if(index == 0){
                Picasso.get().load(ApiClient.IMAGE_URL+ (value.gallery_name))
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.im_slider1)
                    .into(ivChild1)
            }else if(index == 1){
                Picasso.get().load(ApiClient.IMAGE_URL+ (value.gallery_name))
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.im_slider1)
                    .into(ivChild2)
            }else if(index == 2){
                Picasso.get().load(ApiClient.IMAGE_URL+ (value.gallery_name))
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.im_slider1)
                    .into(ivChild3)
            }

        }

        if(place.like == true){
            fabLike.setImageDrawable(resources.getDrawable(R.drawable.ic_love_active))
        }else{
            fabLike.setImageDrawable(resources.getDrawable(R.drawable.ic_love_not_active))
        }

        fabLike.setOnClickListener {
            presenter.like_place(place?.id)
        }

        initToolbar()

    }

    private fun initToolbar() {
        toolbar.title = place.place_name
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsing_toolbar.setTitle("title")
        app_bar_layout.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset -> //  Vertical offset == 0 indicates appBar is fully expanded.
            if (Math.abs(verticalOffset) > 460) {
                appBarExpanded = false
                invalidateOptionsMenu()
            } else {
                appBarExpanded = true
                invalidateOptionsMenu()
            }
        })
    }


    override fun onSuccessLikePlace(status: Boolean) {
        RESULT_CODE = Activity.RESULT_OK
        place.like = status
        if(status == true){
            place.like_count = (place?.like_count?.toInt()?.plus(1)).toString()
            tvLikes.setText(place.like_count)
            fabLike.setImageDrawable(resources.getDrawable(R.drawable.ic_love_active))
        }else{
            place.like_count = (place?.like_count?.toInt()?.minus(1)).toString()
            tvLikes.setText(place.like_count)
            fabLike.setImageDrawable(resources.getDrawable(R.drawable.ic_love_not_active))
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}