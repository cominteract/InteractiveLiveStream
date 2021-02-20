package com.ainsigne.interactivelivestreaming.interfaces
import androidx.lifecycle.LiveData
import com.ainsigne.interactivelivestreaming.model.StreamChat
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.interactivelivestreaming.model.Streams
import com.ainsigne.interactivelivestreaming.utils.SharedConfig


/**
 * Common interface for the repositories so they can be interswapped for mocking objects
 */

interface UserRepository {
    /**
     * registers the user when not logged
     * @param userName to register
     */
    fun signupUser(userName:String)
    /**
     * retrieves the current user from username provided
     * @param userName as [String] username to identify the StreamUser
     * @return LiveData<StreamUser?> the user when it exists
     */
    fun getCurrentUser(userName:String) : LiveData<StreamUser?>
    /**
     * caches the user after signing up
     */
    fun createUser(streamUser: StreamUser)
}

interface StreamRepository {
    /**
     * retrieves the streams already cached
     * @return LiveData<List<Streams>> all the streams currently available
     */
    fun getStreams() : LiveData<List<Streams>>

    /**
     * creates the stream and post on server and then cache locally
     * @param streams as [Streams] to be posted on the server and cached locally
     */
    fun createStream(streams: Streams)

    /**
     * retrieves the current user from username provided
     * @param userName as [String] username to identify the StreamUser
     * @return LiveData<StreamUser?> the user when it exists
     */
    fun getCurrentUser(userName : String) : LiveData<StreamUser?>
    /**
     * updates the stream on server and then cache locally
     * @param streams as [Streams] to be updated on the server and cached locally
     */
    fun updateStream(streams: Streams)

    /**
     * handles fetching of streams to be updated constantly
     */
    fun fetchStreams()
}

interface StreamDetailRepository {
    /**
     * retrieves the chat messages cached identified through its channel
     * @return LiveData<List<StreamChat>> the observable list of chat messages
     */
    fun getChatsFromChannel(channel : String) : LiveData<List<StreamChat>>

    /**
     * creates the chat to be cached locally
     * @param chat as [StreamChat] to be cached locally
     */
    fun sendChat(chat : StreamChat)
    /**
     * retrieves the current user from username provided
     * @param userName as [String] username to identify the StreamUser
     * @return LiveData<StreamUser?> the user when it exists
     */
    fun getCurrentUser(username : String) : LiveData<StreamUser>
}