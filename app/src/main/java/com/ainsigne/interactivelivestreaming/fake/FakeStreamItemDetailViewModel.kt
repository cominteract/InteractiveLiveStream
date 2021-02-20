package com.ainsigne.interactivelivestreaming.fake

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ainsigne.interactivelivestreaming.model.StreamChat
import com.ainsigne.interactivelivestreaming.model.StreamUser

class FakeStreamItemDetailViewModel (
        private val repo : FakeStreamItemDetailRepository
): ViewModel() {



    fun getChatsFromChannel(channel : String) : LiveData<List<StreamChat>> {
        return repo.getChatsFromChannel(channel)
    }
    fun getCurrentUser(username : String) : LiveData<StreamUser> {
        return repo.getCurrentUser(username)
    }

    fun createChat(streamChat : StreamChat){
        repo.sendChat(streamChat)
    }
}