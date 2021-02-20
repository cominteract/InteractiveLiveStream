package com.ainsigne.interactivelivestreaming.fake

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.interactivelivestreaming.model.Streams

class FakeStreamItemViewModel(private val repo : FakeStreamItemRepository) : ViewModel() {
    val streams = repo.allStreams

    var user = MutableLiveData<StreamUser>()

    fun createStream(stream : Streams){
        repo.createStream(stream)
    }

    fun updateStream(stream : Streams){
        repo.updateStream(stream)
    }


    fun getAllStreamsCache() : LiveData<List<Streams>>{
        return repo.getStreams()
    }

    fun getAllStreams(){
        repo.fetchStreams()
    }


    fun getCurrentUser(username : String) : LiveData<StreamUser?> {
        return repo.getCurrentUser(username)
    }

}