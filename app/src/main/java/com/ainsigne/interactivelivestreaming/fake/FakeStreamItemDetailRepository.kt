package com.ainsigne.interactivelivestreaming.fake

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ainsigne.interactivelivestreaming.data.StreamDetailsDao
import com.ainsigne.interactivelivestreaming.interfaces.StreamDetailRepository
import com.ainsigne.interactivelivestreaming.model.StreamChat
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.interactivelivestreaming.model.Streams
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class FakeStreamItemDetailRepository() : StreamDetailRepository  {
    var allChats = MutableLiveData<List<StreamChat>>()
    var allUsers = MutableLiveData<List<StreamUser>>()
    var userList = ArrayList<StreamUser>()
    var chatList = ArrayList<StreamChat>()
    init {
        userList.clear()
        chatList.clear()
        for (i in 0 until  5){
            var streamUser = StreamUser("user$i",
                Date().toStringFormat())
            userList.add(streamUser)
            var streamChat = StreamChat("id$i", "randomMessage$i", "channel$i","user$i", Date().toStringFormat())
            chatList.add(streamChat)
        }
    }


    override fun getChatsFromChannel(channel: String): LiveData<List<StreamChat>> {
        allChats.value = chatList
        var allChatsFromChannel = MutableLiveData<List<StreamChat>>()
        if(!allChats.value?.filter { it.channel == channel }.isNullOrEmpty()){
            allChatsFromChannel.value = allChats.value?.filter { it.channel == channel }
        }
        return allChatsFromChannel
    }

    override fun sendChat(chat: StreamChat) {
        CoroutineScope(Dispatchers.IO).launch {
            chatList.add(chat)
            allChats.value = chatList
        }
    }

    override fun getCurrentUser(username : String) : LiveData<StreamUser> {
        allUsers.value = userList
        var user = allUsers.value?.filter { it.userName == username }?.first()
        var liveData = MutableLiveData<StreamUser>()
        liveData.value = user
        return liveData
    }


}