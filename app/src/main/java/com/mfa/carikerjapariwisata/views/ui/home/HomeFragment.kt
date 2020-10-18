package com.mfa.carikerjapariwisata.views.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.adapter.MainPlaceAdapter
import com.mfa.carikerjapariwisata.adapter.PlaceAdapter
import com.mfa.carikerjapariwisata.utils.RecyclerViewAnimation
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    // inisialisasi binding dan viewmodel
    private lateinit var viewModel: HomeViewModel
    val helper: SnapHelper = PagerSnapHelper()
    val helper2: SnapHelper = PagerSnapHelper()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        viewModel.data.observe({ lifecycle }, {
            val placeAdapter = PlaceAdapter(it)
            val mainPlaceAdapter = MainPlaceAdapter(it)
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

            rvMainPlaces.post(Runnable { // Shift the view to snap  near the center of the screen.
                // This does not have to be precise.
                val dx: Int =
                    (rvMainPlaces.getWidth() - rvMainPlaces.getChildAt(0).getWidth()) / 2
                rvMainPlaces.scrollBy(-dx, 0)
                // Assign the LinearSnapHelper that will initially snap the near-center view.

                helper2.attachToRecyclerView(rvMainPlaces)
            })

            helper.attachToRecyclerView(rvPlaces)
        })

        val data = viewModel.response.observe({ lifecycle }, {
            if (it.isNotEmpty()) {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        })

        return root
    }
}