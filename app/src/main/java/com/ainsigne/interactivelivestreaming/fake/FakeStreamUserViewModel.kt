package com.ainsigne.interactivelivestreaming.fake

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.interactivelivestreaming.viewmodel.StreamUserRepository

class FakeStreamUserViewModel (private val userRepository: FakeStreamUserRepository): ViewModel() {

    var signedUser = getCurrentUser()
    var signedStreamUser = MutableLiveData<StreamUser?>()
    fun getCurrentUser() : LiveData<String?>{
        var mutable = MutableLiveData<String?>()
        mutable = userRepository.signedUser
        return mutable
    }

    fun signupUser(userName: String) {
        userRepository.signupUser(userName)
    }

    fun getUser(userName : String) : LiveData<StreamUser?>  {
        return userRepository.getCurrentUser(userName)
    }




}