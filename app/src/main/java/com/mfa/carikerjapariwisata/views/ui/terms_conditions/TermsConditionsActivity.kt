package com.mfa.carikerjapariwisata.views.ui.terms_conditions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mfa.carikerjapariwisata.R
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import kotlinx.android.synthetic.main.activity_terms_conditions.*
import kotlinx.android.synthetic.main.activity_terms_conditions.bt_close
import kotlinx.android.synthetic.main.activity_terms_conditions.webView

class TermsConditionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_conditions)

        bt_close.setOnClickListener {
            finish()
        }

        webView.loadUrl("file:///android_asset/terms_conditions.html");
    }
}