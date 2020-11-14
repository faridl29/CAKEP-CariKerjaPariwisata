package com.mfa.carikerjapariwisata.utils

import android.content.Context
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.snackbar.Snackbar
import com.mfa.carikerjapariwisata.R
import kotlinx.android.synthetic.main.activity_sign_in.*

class GlobalFunction(private var con: Context) {
    public fun createSnackBar(layout: NestedScrollView, message: String, color: Int){
        val snack: Snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG)
        val view: View = snack.view
        view.setBackgroundColor(ContextCompat.getColor(con, color))
        val params: FrameLayout.LayoutParams = view.getLayoutParams() as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.setLayoutParams(params)
        snack.show()
    }

    public fun handleDrawableClickEditText(editText: EditText){
        editText.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= editText.getRight() - editText.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    if (editText.transformationMethod == null) {
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                            null, null, ContextCompat.getDrawable(
                                con,
                                R.drawable.ic_visibility
                            ), null
                        )
                        editText.setTransformationMethod(PasswordTransformationMethod.getInstance())
                    } else {
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                            null, null, ContextCompat.getDrawable(
                                con,
                                R.drawable.ic_visibility_off
                            ), null
                        )
                        editText.setTransformationMethod(null)
                    }
                    editText.setSelection(editText.length())
                    return@OnTouchListener true
                }
            }
            false
        })
    }
}