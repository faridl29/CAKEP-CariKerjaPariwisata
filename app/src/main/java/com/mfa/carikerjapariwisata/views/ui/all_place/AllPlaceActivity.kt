package com.mfa.carikerjapariwisata.views.ui.all_place

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.SkeletonScreen
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.adapter.PlaceAdapter
import com.mfa.carikerjapariwisata.model.Place
import com.mfa.carikerjapariwisata.utils.GlobalFunction
import com.mfa.carikerjapariwisata.utils.GridSpacingItemDecoration
import com.mfa.carikerjapariwisata.views.ui.place_detail.PlaceDetailActivity
import kotlinx.android.synthetic.main.activity_all_job.iv_clear_text
import kotlinx.android.synthetic.main.activity_all_job.layout
import kotlinx.android.synthetic.main.activity_all_job.searchView
import kotlinx.android.synthetic.main.activity_all_place.*


class AllPlaceActivity : AppCompatActivity(), AllPlaceView {
    private lateinit var presenter: AllPlacePresenter
    private lateinit var globalFunction: GlobalFunction
    private var placeAdapter: PlaceAdapter? = null
    private var skeletonScreen: SkeletonScreen? = null
    private var category: Int? = 0
    private var index: Int = 0
    private var RESULT_CODE = 0
    private var spacing = 40

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_place)

        category = intent.getIntExtra("category_id", 0)

        globalFunction = GlobalFunction(this)

        presenter = AllPlacePresenter(this)
        onAttachView()

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        searchView.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                return@OnEditorActionListener true
            }
            false
        })

        iv_clear_text.setOnClickListener {
            searchView.text = null
        }

        /*hide/show clear button in search view*/

        val searchViewTextWatcher: TextWatcher = object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (placeAdapter != null){
                    placeAdapter?.getFilter()?.filter(s)
                    if (s.toString().trim { it <= ' ' }.isEmpty()) {
                        iv_clear_text.setVisibility(View.GONE)
                    } else {
                        iv_clear_text.setVisibility(View.VISIBLE)
                    }
                }

                return
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        }

        /*hide/show clear button in search view*/
        searchView.addTextChangedListener(
            searchViewTextWatcher
        )
    }

    override fun onSuccess(result: List<Place>) {
        placeAdapter = PlaceAdapter(result, "AllPlaceActivity")

        val layoutManager = GridLayoutManager(this, 2)
        val spanCount = 2; // 2 columns
        val spacing = spacing
        val includeEdge = true

        rvPlaces.apply {
            this.adapter = placeAdapter
            this.layoutManager = layoutManager
            this.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
            this.hasFixedSize()
        }

        placeAdapter?.setOnItemClickCallback(object : PlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Place) {
                val intent = Intent(this@AllPlaceActivity, PlaceDetailActivity::class.java)
                intent.putExtra(PlaceDetailActivity.EXTRA_PLACE, data)
                startActivityForResult(intent, 1)
            }
        })

        placeAdapter?.setOnItemClickCallbackLike(object : PlaceAdapter.OnItemClickCallbackLike{
            override fun onItemClicked(
                data: Place,
                position: Int
            ) {
                presenter.like_place(data.id)
                index = position
            }
        })

        placeAdapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                if(placeAdapter?.itemCount!! > 0){
                    lytSearhEmpty.visibility = View.GONE
                }else{
                    lytSearhEmpty.visibility = View.VISIBLE
                }
            }
        })

    }

    override fun onFailed(error: String) {
        globalFunction.createSnackBar(layout, error, R.color.red,"error")
    }

    override fun onEmpty() {
        lytListEmpty.visibility = View.VISIBLE
    }

    override fun onSuccessLikePlace(status: Boolean) {
        RESULT_CODE = RESULT_OK
        placeAdapter?.like_clicked(index, status)
    }

    override fun onFailedLikePlace(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun onShowLoading() {

    }

    override fun onHideLoading() {

    }

    override fun onAttachView() {
        presenter.onAttach(this)
        presenter.getPlaceList(category)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                spacing = 0
                presenter.getPlaceList(category)
                RESULT_CODE = Activity.RESULT_OK
            }
        }
    }
}