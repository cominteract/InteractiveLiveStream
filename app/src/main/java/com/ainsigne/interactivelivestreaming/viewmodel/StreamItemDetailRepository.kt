package com.ainsigne.interactivelivestreaming.viewmodel

import androidx.lifecycle.LiveData
import com.ainsigne.interactivelivestreaming.data.StreamDetailsDao
import com.ainsigne.interactivelivestreaming.interfaces.StreamDetailRepository
import com.ainsigne.interactivelivestreaming.model.StreamChat
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.interactivelivestreaming.model.Streams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StreamItemDetailRepository(var streamDetails: StreamDetailsDao) : StreamDetailRepository  {
    /**
     * retrieves the chat messages cached identified through its channel
     * @return LiveData<List<StreamChat>> the observable list of chat messages
     */
    override fun getChatsFromChannel(channel: String): LiveData<List<StreamChat>> {
        return streamDetails.getStreamChatsFromChannel(channel)
    }
    /**
     * creates the chat to be cached locally
     * @param chat as [StreamChat] to be cached locally
     */
    override fun sendChat(chat: StreamChat) {
        CoroutineScope(Dispatchers.IO).launch {
            streamDetails.sendChat(chat)
        }
    }
    /**
     * retrieves the current user from username provided
     * @param userName as [String] username to identify the StreamUser
     * @return LiveData<StreamUser?> the user when it exists
     */
    override fun getCurrentUser(username : String) : LiveData<StreamUser> {
        return streamDetails.getUser(username)
    }


}