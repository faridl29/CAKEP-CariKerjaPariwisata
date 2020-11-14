package com.mfa.carikerjapariwisata.views.ui.signin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.mfa.carikerjapariwisata.MainActivity
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.utils.GlobalFunction
import com.mfa.carikerjapariwisata.views.ui.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity(), SignInView {
    private lateinit var presenter: SignInPresenter
    private var globalFunction: GlobalFunction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        globalFunction = GlobalFunction(this)

        presenter = SignInPresenter(this)
        onAttachView()

        onClickHandler()

    }

    fun onClickHandler(){
        btnSignin.setOnClickListener {
            validate_field()
        }

        tvSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        //disable long click for handle copy and paste
        etPassword.setLongClickable(false)

        //action onclick drawable right to hide and show password
        globalFunction?.handleDrawableClickEditText(etPassword)

    }

    private fun validate_field(){
        if(TextUtils.isEmpty(etEmail.text)){
            etEmail.setError("Kolom email harus diisi!")
            return
        }

        if(TextUtils.isEmpty(etPassword.text)){
            etPassword.setError("Kolom password harus diisi!")
            return
        }

        if(!TextUtils.isEmpty(etEmail.text) && !TextUtils.isEmpty(etPassword.text)){
            presenter.loginRequest(etEmail.text.toString(), etPassword.text.toString())
        }
    }

    override fun onSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("request_code", MainActivity.REQUEST_CODE)
        startActivity(intent)

        finish()
    }

    override fun onFailed(error: String) {
        globalFunction?.createSnackBar(layout, error, R.color.red)
    }

    override fun onLogged() {
        val a = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(a)
        finish()
    }

    override fun onAttachView() {
        presenter.onAttach(this)
        presenter.cekLogin()
    }

    override fun onDetachView() {
        presenter.onDetach()
    }

    override fun onDestroy() {
        onDetachView()
        super.onDestroy()
    }
}