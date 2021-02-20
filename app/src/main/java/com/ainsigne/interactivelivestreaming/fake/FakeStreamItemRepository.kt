package com.ainsigne.interactivelivestreaming.fake

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ainsigne.interactivelivestreaming.api.StreamsService
import com.ainsigne.interactivelivestreaming.data.StreamItemsDao
import com.ainsigne.interactivelivestreaming.interfaces.StreamRepository
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.interactivelivestreaming.model.Streams
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class FakeStreamItemRepository() : StreamRepository {

    var allStreams = MutableLiveData<List<Streams>>()

    init {

    }


    override fun getStreams(): LiveData<List<Streams>> {
        return allStreams
    }

    override fun createStream(streams: Streams) {
        CoroutineScope(Dispatchers.IO).launch {
            var streamList : ArrayList<Streams> = allStreams.value as ArrayList<Streams>
            streamList.add(streams)
            allStreams.value = streamList
        }
    }

    override fun updateStream(streams: Streams) {
        CoroutineScope(Dispatchers.IO).launch {
            var streamList : ArrayList<Streams> = allStreams.value as ArrayList<Streams>
            var index = 0;
            for (i in 0 until streamList.size){
                if(streams.channel == streamList[i].channel){
                    index = i
                    break;
                }
            }
            streamList.set(index, streams)
            allStreams.value = streamList
        }
    }

    override fun fetchStreams() {
        CoroutineScope(Dispatchers.IO).launch {
            var streamList = ArrayList<Streams>()
            for (i in 0 until  5){
                var stream = Streams(channel = UUID.randomUUID().toString(), StreamUser("user$i",
                    Date().toStringFormat()), Date().toStringFormat(), true, "data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUA\n" +
                        "    AAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO\n" +
                        "        9TXL0Y4OHwAAAABJRU5ErkJggg=="
                    , (i * 11111)
                )
                streamList.add(stream)
            }
            allStreams.value = streamList
        }
    }

    override fun getCurrentUser(userName : String): LiveData<StreamUser?> {
        var user = allStreams.value?.filter { it.user.userName == userName }?.first()?.user
        var liveData = MutableLiveData<StreamUser>()
        liveData.value = user
        return liveData
    }


}