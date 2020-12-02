package com.mfa.carikerjapariwisata.views.ui.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.views.ui.edit_profile.EditProfileActivity
import com.mfa.carikerjapariwisata.views.ui.posted_job.PostedJobActivity
import com.mfa.carikerjapariwisata.views.ui.privacy_policy.PrivacyPolicyActivity
import com.mfa.carikerjapariwisata.views.ui.signin.SignInActivity
import com.mfa.carikerjapariwisata.views.ui.terms_conditions.TermsConditionsActivity
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ProfileView {
    private var presenter: ProfilePresenter? = null
    private var REQUEST_CODE = 11
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        presenter = ProfilePresenter(root.context)
        onAttachView()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter?.getDataUser()

        lytPostedJob.setOnClickListener {
            val intent = Intent(activity, PostedJobActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            logout()
        }

        tvEditProfile.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        lytPrivacyPolicy.setOnClickListener {
            val intent = Intent(activity, PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }

        lytTermsConditions.setOnClickListener {
            val intent = Intent(activity, TermsConditionsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun logout(){
        SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Apakah anda yakin?")
            .setConfirmButtonBackgroundColor(resources.getColor(R.color.colorPrimary))
            .setContentText("Akan keluar dari aplikasi!")
            .setConfirmText("Ya!")
            .setConfirmClickListener { presenter?.logout() }
            .setCancelButton(
                "Batal"
            ) { sDialog -> sDialog.dismissWithAnimation() }
            .show()
    }

    override fun onSuccessGetData(name: String?, profile: String?, email: String?) {
        Picasso.get().load(ApiClient.PROFILE_URL+ profile)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .error(R.drawable.im_slider1)
            .into(ivProfile)

        tvName.text = name
        tvEmail.text = email
    }

    override fun onLogout() {
        startActivity(
            Intent(activity, SignInActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        activity?.finish()
    }

    override fun onAttachView() {
        presenter?.onAttach(this)
    }

    override fun onDetachView() {
        presenter?.onDetach()
    }

    override fun onDestroy() {
        onDetachView()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                presenter?.getDataUser()
            }
        }
    }
}