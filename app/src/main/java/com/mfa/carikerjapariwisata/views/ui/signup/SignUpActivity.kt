package com.mfa.carikerjapariwisata.views.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.mfa.carikerjapariwisata.MainActivity
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.utils.GlobalFunction
import com.mfa.carikerjapariwisata.views.ui.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.etEmail
import kotlinx.android.synthetic.main.activity_sign_up.etPassword
import kotlinx.android.synthetic.main.activity_sign_up.layout
import org.w3c.dom.Text

class SignUpActivity : AppCompatActivity(), SignUpView {
    private lateinit var presenter: SignUpPresenter
    private var globalFunction: GlobalFunction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        globalFunction = GlobalFunction(this)

        presenter = SignUpPresenter(this)
        onAttachView()

        onClickHandler()
    }

    private fun onClickHandler(){
        btnSignup.setOnClickListener {
            validate_field()
        }

        tvSignin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        //disable long click for handle copy and paste
        etPassword.setLongClickable(false)
        etConfirmPassword.setLongClickable(false)

        //action onclick drawable right to hide and show password
        globalFunction?.handleDrawableClickEditText(etPassword)
        globalFunction?.handleDrawableClickEditText(etConfirmPassword)
    }

    private fun validate_field(){
        val email = etEmail.text
        val password = etPassword.text
        val confirmPassword = etConfirmPassword.text
        val fullname = etFullname.text

        if(TextUtils.isEmpty(email)){
            etEmail.setError("Kolom email harus diisi!")
            return
        }

        if(TextUtils.isEmpty(password)){
            etPassword.setError("Kolom password harus diisi!")
            return
        }

        if(password.toString().length < 8){
            etPassword.setError("Password minimal 8 karakter!")
            return
        }

        if(TextUtils.isEmpty(confirmPassword)){
            etConfirmPassword.setError("Kolom Konfirmasi password harus diisi!")
            return
        }

        if(TextUtils.isEmpty(fullname)){
            etFullname.setError("Kolom nama lengkap harus diisi!")
            return
        }

        if(!password.toString().equals(confirmPassword.toString())){
            etConfirmPassword.setError("Password tidak cocok!")
            return
        }

        if(!cbPrivacyPolicy.isChecked){
            globalFunction?.createSnackBar(layout, "You must accept out privacy policy", R.color.green)
        }

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(fullname) && password.toString().equals(confirmPassword.toString()) && cbPrivacyPolicy.isChecked && password.toString().length >= 8){
            presenter.signUpRequest(email.toString(), password.toString(), fullname.toString())
        }
    }

    override fun onSuccess() {
        val globalFunction = GlobalFunction(this)
        val intent = Intent(this, SignInActivity::class.java)
        intent.putExtra("request_code", SignInActivity.REQUEST_CODE)
        startActivity(intent)
    }

    override fun onFailed(error: String) {
        val globalFunction = GlobalFunction(this)
        globalFunction.createSnackBar(layout, error, R.color.red)
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
}