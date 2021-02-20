package com.ainsigne.interactivelivestreaming.fake

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ainsigne.interactivelivestreaming.api.StreamsService
import com.ainsigne.interactivelivestreaming.data.OnboardingDao
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class FakeStreamUserRepository()  {

    var signedUser = MutableLiveData<String?>()
    var signedStreamUsers = MutableLiveData<List<StreamUser>>()
    var userList = ArrayList<StreamUser>()
    init {

        userList.clear()
        for (i in 0 until  5){
            var streamUser = StreamUser("user$i",
                Date().toStringFormat())
            userList.add(streamUser)
        }

    }

    fun signupUser(userName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            signedUser.value = userName
        }

        var signedStreamUser = StreamUser(userName, Date().toStringFormat())

        createUser(signedStreamUser)
    }

    fun getCurrentUser(userName : String) : LiveData<StreamUser?>{
        signedStreamUsers.value = userList
        var user = signedStreamUsers.value?.filter { it.userName == userName }?.first()
        var liveData = MutableLiveData<StreamUser>()
        liveData.value = user
        return liveData

    }

    fun createUser(streamUser: StreamUser) {
        var streamUsers : ArrayList<StreamUser>  = signedStreamUsers.value as ArrayList<StreamUser>
        streamUsers.add(streamUser)
        signedStreamUsers.value = streamUsers
        signedUser.value = streamUser.userName
    }
}