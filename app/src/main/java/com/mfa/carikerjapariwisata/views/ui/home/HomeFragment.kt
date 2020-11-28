package com.mfa.carikerjapariwisata.views.ui.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.adapter.MainPlaceAdapter
import com.mfa.carikerjapariwisata.adapter.PlaceAdapter
import com.mfa.carikerjapariwisata.model.Place
import com.mfa.carikerjapariwisata.utils.RecyclerViewAnimation
import com.mfa.carikerjapariwisata.views.ui.all_place.AllPlaceActivity
import com.mfa.carikerjapariwisata.views.ui.place_detail.PlaceDetailActivity
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(), HomeView {

    private lateinit var presenter: HomePresenter
    private lateinit var root: View
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var mainPlaceAdapter: MainPlaceAdapter
    private var index: Int = 0
    private val helper: SnapHelper = PagerSnapHelper()
    private val helper2: SnapHelper = PagerSnapHelper()

    companion object{
        var TAG = "1"
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_home, container, false)

        presenter = HomePresenter(root.context)

        onAttachView()

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        lytSejarah.setOnClickListener {
            var intent = Intent(activity, AllPlaceActivity::class.java)
            intent.putExtra("category_id", 1)
            startActivityForResult(intent, 1)
        }

        lytAlam.setOnClickListener {
            var intent = Intent(activity, AllPlaceActivity::class.java)
            intent.putExtra("category_id", 2)
            startActivityForResult(intent, 1)
        }

        lytPendidikan.setOnClickListener {
            var intent = Intent(activity, AllPlaceActivity::class.java)
            intent.putExtra("category_id", 3)
            startActivityForResult(intent, 1)
        }

        lytReligi.setOnClickListener {
            var intent = Intent(activity, AllPlaceActivity::class.java)
            intent.putExtra("category_id", 4)
            startActivityForResult(intent, 1)
        }

    }

    override fun onSuccess(result: List<Place>) {
        placeAdapter = PlaceAdapter(result, "HomeFragment")

        rvPlaces.apply {
            this.adapter = placeAdapter
            this.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            this.hasFixedSize()
        }
        placeAdapter.setOnItemClickCallback(object : PlaceAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Place) {
                val intent = Intent(activity, PlaceDetailActivity::class.java)
                intent.putExtra(PlaceDetailActivity.EXTRA_PLACE, data)
                startActivityForResult(intent, 1)
            }

        })

        placeAdapter.setOnItemClickCallbackLike(object : PlaceAdapter.OnItemClickCallbackLike{
            override fun onItemClicked(
                data: Place,
                position: Int
            ) {
                presenter.like_place(data.id)
                index = position
            }

        })
    }

    override fun onSuccessGetFavoritestPlace(result: List<Place>) {
        mainPlaceAdapter = MainPlaceAdapter(result)
        val layoutManager =
            RecyclerViewAnimation(activity, LinearLayoutManager.HORIZONTAL, false)

        rvMainPlaces.apply {
            this.adapter = mainPlaceAdapter
            this.layoutManager = layoutManager
            this.hasFixedSize()
        }

        indicator.attachToRecyclerView(rvMainPlaces)

        mainPlaceAdapter.setOnItemClickCallback(object : MainPlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Place) {
                val intent = Intent(activity, PlaceDetailActivity::class.java)
                intent.putExtra(PlaceDetailActivity.EXTRA_PLACE, data)
                startActivityForResult(intent, 1)
            }

        })

        rvMainPlaces.post(Runnable { // Shift the view to snap  near the center of the screen.
            // This does not have to be precise.
            val dx: Int =
                (rvMainPlaces.width - rvMainPlaces.getChildAt(0).width) / 2
            rvMainPlaces.scrollBy(-dx, 0)
            // Assign the LinearSnapHelper that will initially snap the near-center view.

            helper2.attachToRecyclerView(rvMainPlaces)
        })
    }

    override fun onFailed(error: String) {
        Toast.makeText(root.context, error, Toast.LENGTH_SHORT).show()
    }

    override fun onEmpty() {
        TODO("Not yet implemented")
    }

    override fun onSuccessLikePlace(status: Boolean) {
        placeAdapter.like_clicked(index, status)
    }

    override fun onFailedLikePlace(error: String) {
        placeAdapter.notifyDataSetChanged()
        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
    }

    override fun onAttachView() {
        presenter.onAttach(this)
        presenter.getPlaceList(0)
        presenter.getFavoritePlace()
    }

    override fun onDetachView() {
        presenter.onDetach()
    }

    override fun onDestroy() {
        onDetachView()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            if(resultCode == RESULT_OK){
                presenter.getPlaceList(0)
                presenter.getFavoritePlace()
            }
        }
    }

}