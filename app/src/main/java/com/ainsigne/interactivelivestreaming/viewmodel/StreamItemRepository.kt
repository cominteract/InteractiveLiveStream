package com.ainsigne.interactivelivestreaming.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ainsigne.interactivelivestreaming.api.StreamsService
import com.ainsigne.interactivelivestreaming.data.StreamItemsDao
import com.ainsigne.interactivelivestreaming.interfaces.StreamRepository
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.interactivelivestreaming.model.Streams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StreamItemRepository(var streamDao: StreamItemsDao,private val streamsService : StreamsService) : StreamRepository {

    var allStreams = MutableLiveData<List<Streams>>()

    /**
     * retrieves the streams already cached
     * @return LiveData<List<Streams>> all the streams currently available
     */
    override fun getStreams(): LiveData<List<Streams>> {
        return streamDao.getStreams()
    }
    /**
     * creates the stream and post on server and then cache locally
     * @param streams as [Streams] to be posted on the server and cached locally
     */
    override fun createStream(streams: Streams) {
        CoroutineScope(Dispatchers.IO).launch {
            streamDao.createStream(streams)
            streamsService.postStream(streams)
        }
    }
    /**
     * updates the stream on server and then cache locally
     * @param streams as [Streams] to be updated on the server and cached locally
     */
    override fun updateStream(streams: Streams) {
        CoroutineScope(Dispatchers.IO).launch {
            streamDao.createStream(streams)
            streamsService.updateStream(streams, streams.channel)
        }
    }
    /**
     * handles fetching of streams to be updated constantly
     */
    override fun fetchStreams() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = streamsService.getAllStreams()
            if(response.isSuccessful){
                response.body()?.let { streams ->
                    for(stream in streams){
                        streamDao.createStream(stream)
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        allStreams.value = streams
                    }
                }
            }
        }
    }
    /**
     * retrieves the current user from username provided
     * @param userName as [String] username to identify the StreamUser
     * @return LiveData<StreamUser?> the user when it exists
     */
    override fun getCurrentUser(userName : String): LiveData<StreamUser?> {
        return streamDao.getUser(userName)
    }


}