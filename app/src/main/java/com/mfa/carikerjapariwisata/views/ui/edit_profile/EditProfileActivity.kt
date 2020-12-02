package com.mfa.carikerjapariwisata.views.ui.edit_profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.mfa.carikerjapariwisata.BuildConfig
import com.mfa.carikerjapariwisata.MainActivity
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.model.SharedPrefManager
import com.mfa.carikerjapariwisata.views.ui.signin.SignInActivity
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_all_job.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.bt_close
import kotlinx.android.synthetic.main.activity_edit_profile.etEmail
import kotlinx.android.synthetic.main.activity_edit_profile.etFullname
import kotlinx.android.synthetic.main.activity_sign_up.*

class EditProfileActivity : AppCompatActivity(), EditProfileView {
    private lateinit var presenter: EditProfilePresenter
    private lateinit var sharedPrefManager: SharedPrefManager
    private var filePath: String? = null
    private var fileName: String? = null
    private val PICK_PHOTO = 1958
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var mImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        sharedPrefManager = SharedPrefManager(this)

        presenter = EditProfilePresenter(this)
        onAttachView()

        etFullname.setText(sharedPrefManager.spFullName)
        etEmail.setText(sharedPrefManager.spEmail)
        etTelepon.setText(sharedPrefManager.spTelepon)

        if(sharedPrefManager.spProfile  != ""){
            ivProfile.setPadding(0,0,0,0)
            Picasso.get().load(ApiClient.PROFILE_URL + sharedPrefManager.spProfile)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .error(R.drawable.ic_person)
                .into(ivProfile)
        }

        ivPickPhoto.setOnClickListener {
            addPhoto()
        }

        btnSave.setOnClickListener {
            validate_field()
        }

        bt_close.setOnClickListener {
            finish()
        }

        verifyStoragePermissions(this)
    }

    fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have write permission
        val permission: Int = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    fun addPhoto() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAutoZoomEnabled(true)
            .setAspectRatio(1, 1)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setRequestedSize(1280, 1280)
            .start(this)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                filePath = resultUri.path
                fileName = filePath?.lastIndexOf("/")?.plus(1)?.let { filePath?.substring(it) }
                mImageUri = resultUri
                Picasso.get().load(mImageUri)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.mipmap.ic_launcher)
                    .into(ivProfile)
                Picasso.get().invalidate(mImageUri)
                ivProfile.setPadding(0, 0, 0, 0)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                if (BuildConfig.DEBUG) error.printStackTrace()
            }
        }
    }

    private fun validate_field(){
        val email = etEmail.text
        val fullname = etFullname.text
        val telepon = etTelepon.text

        if(TextUtils.isEmpty(email)){
            etEmail.setError("Kolom email harus diisi!")
            return
        }

        if(TextUtils.isEmpty(fullname)){
            etFullname.setError("Kolom nama harus diisi!")
            return
        }

        if(TextUtils.isEmpty(telepon)){
            etTelepon.setError("Kolom telepon lengkap harus diisi!")
            return
        }

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(fullname) && !TextUtils.isEmpty(telepon) ){
            presenter.editProfile(
                fullname.toString(),
                email.toString(),
                telepon.toString(),
                if (fileName != null) fileName else sharedPrefManager.spProfile,
                filePath
            )
        }
    }

    override fun onSuccess(name: String?, email: String?, telepon: String?, profile: String?) {
        sharedPrefManager.saveSPString(SharedPrefManager.SP_FULLNAME, name)
        sharedPrefManager.saveSPString(SharedPrefManager.SP_EMAIL, email)
        sharedPrefManager.saveSPString(SharedPrefManager.SP_TELEPON, telepon)
        sharedPrefManager.saveSPString(SharedPrefManager.SP_PROFILE, profile)
        btnSave.doneLoadingAnimation(resources.getColor(R.color.colorPrimary), BitmapFactory.decodeResource(resources,
            R.drawable.ic_done_white_48dp))
        val handler = Handler()
        handler.postDelayed(Runnable {
            var intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }, 1000)
    }

    override fun onFailed(error: String) {
        btnSave.revertAnimation()
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun onLoading() {
        btnSave.startAnimation()
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