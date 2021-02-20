package com.ainsigne.interactivelivestreaming.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ainsigne.interactivelivestreaming.api.StreamsService
import com.ainsigne.interactivelivestreaming.data.OnboardingDao
import com.ainsigne.interactivelivestreaming.interfaces.UserRepository
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class StreamUserRepository(var onboarding : OnboardingDao, var streamsService: StreamsService) : UserRepository {

    var signedUser = MutableLiveData<String?>()
    var signedStreamUser = MutableLiveData<StreamUser?>()


    /**
     * registers the user when not logged
     * @param userName to register
     */
    override fun signupUser(userName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = streamsService.signupUser(StreamUser(userName, Date().toStringFormat()))
            if(response.isSuccessful){
                withContext(Dispatchers.Main){
                    signedUser.value =  response.body()?.userName
                }
            }
        }
        createUser(StreamUser(userName, Date().toStringFormat()))
    }
    /**
     * retrieves the current user from username provided
     * @param userName as [String] username to identify the StreamUser
     * @return LiveData<StreamUser?> the user when it exists
     */
    override fun getCurrentUser(userName : String) : LiveData<StreamUser?>{
        return onboarding.getUser(userName)

    }
    /**
     * caches the user after signing up
     */
    override fun createUser(streamUser: StreamUser) {
        CoroutineScope(Dispatchers.IO).launch {
            onboarding.createUser(streamUser)
        }
        signedUser.value = streamUser.userName
    }
}