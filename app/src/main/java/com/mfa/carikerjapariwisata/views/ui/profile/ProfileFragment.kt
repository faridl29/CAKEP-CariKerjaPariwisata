package com.mfa.carikerjapariwisata.views.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.views.ui.signin.SignInActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ProfileView {
    private var presenter: ProfilePresenter? = null
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

        btnLogout.setOnClickListener {
            logout()
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
        presenter?.getDataUser()
    }

    override fun onDetachView() {
        presenter?.onDetach()
    }

    override fun onDestroy() {
        onDetachView()
        super.onDestroy()
    }
}