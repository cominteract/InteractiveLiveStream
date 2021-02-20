package com.ainsigne.interactivelivestreaming.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.interactivelivestreaming.model.Streams
import com.ainsigne.interactivelivestreaming.viewmodel.StreamItemRepository

class StreamItemViewModel(private val repo : StreamItemRepository) : ViewModel() {
    val streams = repo.allStreams
    var isStreamCreated = false
    var user = MutableLiveData<StreamUser>()

    fun createStream(stream : Streams){
        repo.createStream(stream)
        isStreamCreated = true
    }

    fun updateStream(stream : Streams){
        repo.updateStream(stream)
        isStreamCreated = true
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