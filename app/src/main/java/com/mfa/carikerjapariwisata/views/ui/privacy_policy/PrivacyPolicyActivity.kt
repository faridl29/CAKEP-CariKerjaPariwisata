package com.mfa.carikerjapariwisata.views.ui.privacy_policy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mfa.carikerjapariwisata.R
import kotlinx.android.synthetic.main.activity_all_job.*
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import kotlinx.android.synthetic.main.activity_privacy_policy.bt_close

class PrivacyPolicyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        webView.loadUrl("file:///android_asset/privacy_policy.html")
        bt_close.setOnClickListener {
            finish()
        }
    }
}