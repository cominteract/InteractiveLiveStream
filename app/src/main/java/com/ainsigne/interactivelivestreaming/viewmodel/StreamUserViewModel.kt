package com.ainsigne.interactivelivestreaming.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.interactivelivestreaming.viewmodel.StreamUserRepository

class StreamUserViewModel (private val userRepository: StreamUserRepository): ViewModel() {

    var signedUser = getCurrentUser()
    var signedStreamUser = MutableLiveData<StreamUser?>()
    fun getCurrentUser() : LiveData<String?>{
        var mutable = MutableLiveData<String?>()
        var mutableStreamUser = MutableLiveData<StreamUser?>()
        mutable = userRepository.signedUser
        mutableStreamUser = userRepository.signedStreamUser
        return mutable
    }

    fun signupUser(userName: String) {
        userRepository.signupUser(userName)
    }

    fun getUser(userName : String) : LiveData<StreamUser?>  {
        return userRepository.getCurrentUser(userName)
    }




}