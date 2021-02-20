package com.ainsigne.interactivelivestreaming.utils

import android.content.SharedPreferences
import com.ainsigne.interactivelivestreaming.ui.OnboardingActivity

class SharedConfig {
    private var sharedPreferences: SharedPreferences? = null
    constructor(sharedPreferences: SharedPreferences){
        this.sharedPreferences = sharedPreferences
    }

    fun saveUser(userLogged : String) {
        val editor = sharedPreferences?.edit()
        editor?.putString(OnboardingActivity.usernameKey, userLogged)
        editor?.apply()
    }

    fun logoutUser() {
        val editor = sharedPreferences?.edit()
        editor?.putString(OnboardingActivity.usernameKey, null)
        editor?.apply()
    }

    fun getUser() : String?{
        return sharedPreferences?.getString(OnboardingActivity.usernameKey, null)
    }


}