package com.mfa.carikerjapariwisata.views.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.adapter.MainPlaceAdapter
import com.mfa.carikerjapariwisata.adapter.PlaceAdapter
import com.mfa.carikerjapariwisata.model.Place
import com.mfa.carikerjapariwisata.utils.RecyclerViewAnimation
import com.mfa.carikerjapariwisata.views.ui.place_detail.PlaceDetail
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(), HomeView {

    private lateinit var presenter: HomePresenter
    private lateinit var root: View
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

    }

    override fun onSuccess(result: List<Place>) {
        val placeAdapter = PlaceAdapter(result)
        val mainPlaceAdapter = MainPlaceAdapter(result)
        val layoutManager =
            RecyclerViewAnimation(activity, LinearLayoutManager.HORIZONTAL, false)

        rvPlaces.apply {
            this.adapter = placeAdapter
            this.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            this.hasFixedSize()
        }

        rvMainPlaces.apply {
            this.adapter = mainPlaceAdapter
            this.layoutManager = layoutManager
            this.hasFixedSize()
        }

        indicator.attachToRecyclerView(rvMainPlaces)

        mainPlaceAdapter.setOnItemClickCallback(object : MainPlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Place) {
                val intent = Intent(activity, PlaceDetail::class.java)
                intent.putExtra(PlaceDetail.EXTRA_PLACE, data)
                startActivity(intent)
            }

        })

        rvMainPlaces.post(Runnable { // Shift the view to snap  near the center of the screen.
            // This does not have to be precise.
            val dx: Int =
                (rvMainPlaces.getWidth() - rvMainPlaces.getChildAt(0).getWidth()) / 2
            rvMainPlaces.scrollBy(-dx, 0)
            // Assign the LinearSnapHelper that will initially snap the near-center view.

            helper2.attachToRecyclerView(rvMainPlaces)
        })

        helper.attachToRecyclerView(rvPlaces)
        presenter.onCleared()
    }

    override fun onFailed(error: String) {
        Toast.makeText(root.context, error, Toast.LENGTH_SHORT).show()
        presenter.onCleared()
    }

    override fun onEmpty() {
        TODO("Not yet implemented")
    }

    override fun onAttachView() {
        presenter.onAttach(this)
        presenter.getPlaceList()
    }

    override fun onDetachView() {
        presenter.onDetach()
    }

    override fun onDestroy() {
        onDetachView()
        super.onDestroy()
    }

}