package com.mfa.carikerjapariwisata.views.ui.apply_job

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.mfa.carikerjapariwisata.R
import kotlinx.android.synthetic.main.activity_apply_job.*

class ApplyJobActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_job)

        addAttachment.setOnClickListener {
            if(attachment4.isVisible){
                attachment5.setVisibility(View.VISIBLE)
                addAttachment.setVisibility(View.GONE)
            }else if(attachment3.isVisible){
                attachment4.setVisibility(View.VISIBLE)
            }else if(attachment2.isVisible){
                attachment3.setVisibility(View.VISIBLE)
            }else if(attachment1.isVisible){
                attachment2.setVisibility(View.VISIBLE)
            }
        }
    }
}