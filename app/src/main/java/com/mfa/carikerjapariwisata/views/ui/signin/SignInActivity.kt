package com.mfa.carikerjapariwisata.views.ui.signin

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
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
    companion object{
        val REQUEST_CODE = "1"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        globalFunction = GlobalFunction(this)

        presenter = SignInPresenter(this)
        onAttachView()

        if(intent.getStringExtra("request_code") == REQUEST_CODE) {
            globalFunction?.createSnackBar(layout, "Registrasi berhasil \n Login untuk masuk ke aplikasi!", R.color.colorPrimary)
        }

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
        btnSignin.doneLoadingAnimation(resources.getColor(R.color.colorPrimary), BitmapFactory.decodeResource(resources,
            R.drawable.ic_done_white_48dp))

        val handler = Handler()
        handler.postDelayed(Runnable { // Do something after 5s = 5000ms

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("request_code", MainActivity.REQUEST_CODE)
            startActivity(intent)

            finish()
        }, 1000)
    }

    override fun onFailed(error: String) {
        btnSignin.revertAnimation()
        globalFunction?.createSnackBar(layout, error, R.color.red)
    }

    override fun onLogged() {
        val a = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(a)
        finish()
    }

    override fun onLoading() {
        btnSignin.startAnimation()
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