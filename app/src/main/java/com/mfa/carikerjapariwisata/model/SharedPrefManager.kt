package com.mfa.carikerjapariwisata.model

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {
	var sp: SharedPreferences
	var spEditor: SharedPreferences.Editor
	fun saveSPString(keySP: String?, value: String?) {
		spEditor.putString(keySP, value)
		spEditor.commit()
	}

	fun saveSPInt(keySP: String?, value: Int) {
		spEditor.putInt(keySP, value)
		spEditor.commit()
	}

	fun saveSPBoolean(keySP: String?, value: Boolean) {
		spEditor.putBoolean(keySP, value)
		spEditor.commit()
	}

	val spId: String?
		get() = sp.getString(SP_ID, "")

	val spEmail: String?
		get() = sp.getString(SP_EMAIL, "")

	val spTelepon: String?
		get() = sp.getString(SP_TELEPON, "")

	val spFullName: String?
		get() = sp.getString(SP_FULLNAME, "")

	val spProfile: String?
		get() = sp.getString(SP_PROFILE, "")

	val spAlreadySignin: Boolean
		get() = sp.getBoolean(SP_ALREADY_SIGNIN, false)

	companion object {
		const val SP_Cakep = "spCakep"
		const val SP_ID = "spId"
		const val SP_EMAIL = "spEmail"
		const val SP_TELEPON = "spTelepon"
		const val SP_FULLNAME = "spFullname"
		const val SP_PROFILE = "spProfile"
		const val SP_ALREADY_SIGNIN = "spAlreadySignin"
	}

	init {
		sp = context.getSharedPreferences(
			SP_Cakep,
			Context.MODE_PRIVATE
		)
		spEditor = sp.edit()
	}
}