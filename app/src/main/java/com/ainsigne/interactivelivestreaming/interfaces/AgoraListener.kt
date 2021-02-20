package com.ainsigne.interactivelivestreaming.interfaces

interface AgoraListener {
    /**
     * when joining a channel is successful
     * @param channell as [String] the channel to identify which stream is joined
     */
    fun onJoinChannelSuccess(channell: String, uid: Int, elapsed: Int)
    /**
     * when streaming was successful on host side
     * it can now save a preview image and create stream to be posted to the server
     * @param unused
     */
    fun onFirstLocalVideoFrame(width: Int, height: Int, elapsed: Int)
    /**
     * when streaming was successful on client side
     * @param unused
     */
    fun onFirstRemoteVideoFrame(width: Int, height: Int, elapsed: Int)
    /**
     * when host streamer has left notify that stream is finished
     */
    fun onHostLeft()
    /**
     * when sending message to the channel is successful
     * it can now be saved locally
     * @param msg as [String] the msg that was sent
     */
    fun sendMessageOnSuccess(msg : String)
    /**
     * when another user sending message to the channel is successful
     * it can now be saved locally
     * @param text as [String] the msg that was sent
     * @param fromUser as [String] then user that sent the message
     */
    fun messageReceivedOnSuccess(text : String, fromUser : String)

    /**
     * when login is successful and can now send realtime message
     */
    fun loginSuccessful()
    /**
     * when login fails
     */
    fun loginFailure()
}