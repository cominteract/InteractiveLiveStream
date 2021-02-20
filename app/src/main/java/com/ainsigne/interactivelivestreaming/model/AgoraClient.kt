package com.ainsigne.interactivelivestreaming.model

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.get
import com.ainsigne.interactivelivestreaming.R
import com.ainsigne.interactivelivestreaming.interfaces.AgoraListener
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtm.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AgoraClient {

    val TAG = "INIT_AGORA"

    private var mRtcEngine: RtcEngine? = null
    private var mRtmClient: RtmClient? = null
    private var mRtmChannel: RtmChannel? = null
    private var agoraListener : AgoraListener? = null
    private var activity : Activity? = null
    fun setAgoraListener(agoraListener: AgoraListener){
        this.agoraListener = agoraListener
    }

    fun setContext(activity: Activity){
        this.activity = activity
    }

    fun setChannelProfile() {
        mRtcEngine?.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
        mRtcEngine?.setClientRole(IRtcEngineEventHandler.ClientRole.CLIENT_ROLE_BROADCASTER);
    }

    fun leaveChannel() {
        // Leave the current channel.
        mRtcEngine?.leaveChannel()
    }

    fun emptyObjects(){
        mRtcEngine = null
        mRtmChannel = null
        mRtmClient = null
    }
    fun setupLocalVideo(frameContainer: FrameLayout) {
        // Create a SurfaceView object.
        val mLocalView: SurfaceView = RtcEngine.CreateRendererView(activity?.applicationContext)
        mLocalView.setZOrderMediaOverlay(true)
        frameContainer.addView(mLocalView)
        // Set the local video view.
        val localVideoCanvas = VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, 0)
        mRtcEngine?.setupLocalVideo(localVideoCanvas)
    }

    fun setupRemoteVideo(uid: Int, frameLayout: FrameLayout) {
        val mRemoteView: SurfaceView = RtcEngine.CreateRendererView(activity)
        frameLayout.addView(mRemoteView)
        // Set the remote video view.
        mRtcEngine?.setupRemoteVideo(VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid))
    }

    fun joinChannel(token : String?, channel : String) {
        // Enable the video module.
        mRtcEngine?.enableVideo()
        // For SDKs earlier than v3.0.0, call this method to enable interoperability between the Native SDK and the Web SDK if the Web SDK is in the channel. As of v3.0.0, the Native SDK enables the interoperability with the Web SDK by default.
        // mRtcEngine?.enableWebSdkInteroperability(true)

        // Join a channel with a token.
        Log.d(" Joining Channel "," Joining Channel ")
        mRtcEngine?.joinChannel(token,
                channel, "Extra Optional Data", 0)
    }


    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        // Listen for the onJoinChannelSuccess callback.
        // This callback occurs when the local user successfully joins the channel.
        override fun onJoinChannelSuccess(channell: String, uid: Int, elapsed: Int) {
            agoraListener?.onJoinChannelSuccess(channell, uid, elapsed)
            activity?.runOnUiThread {
                Log.i("agora", channell + "Message Join channel success, uid: " + (uid))

                try {
                    mRtmChannel = mRtmClient?.createChannel(channell, mRtmChannelListener)
                } catch (e: java.lang.RuntimeException) {
                    Log.e(TAG, "Fails to create channel. Maybe the channel ID is invalid," +
                            " or already in use. See the API Reference for more information.")
                }
                mRtmChannel?.join(object : ResultCallback<Void?> {
                    override fun onSuccess(responseInfo: Void?) {
                        Log.d(TAG, "Message Successfully joins the channel!")
                    }

                    override fun onFailure(errorInfo: ErrorInfo) {
                        Log.d(TAG, "Message join channel failure! errorCode = "
                                + errorInfo.errorDescription)
                    }
                })
            }
        }

        // Listen for the onFirstRemoteVideoDecoded callback.
        // This callback occurs when the first video frame of the host is received and decoded after the host successfully joins the channel.
        // You can call the setupRemoteVideo method in this callback to set up the remote video view.
        override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
            agoraListener?.onFirstRemoteVideoFrame(width, height, elapsed)
            Log.i("agora", "User remote decoded " + uid)
                //setupRemoteVideo(uid)
        }

        override fun onFirstLocalVideoFrame(width: Int, height: Int, elapsed: Int) {
            Log.i("agora", "User local decoded " + elapsed)
            agoraListener?.onFirstLocalVideoFrame(width, height, elapsed)
        }

        override fun onWarning(warn: Int) {
            super.onWarning(warn)
            Log.i("agora", "User error " + warn)
        }

        override fun onError(err: Int) {
            super.onError(err)
            Log.i("agora", "User error " + err)
        }

        // Listen for the onUserOffline callback.
        // This callback occurs when the host leaves the channel or drops offline.
        override fun onUserOffline(uid: Int, reason: Int) {
            Log.i("agora", "User offline, uid: " + (uid))
            agoraListener?.onHostLeft()
                //onRemoteUserLeft()
        }
    }


    fun initializeChat(){
        try {
            mRtmClient = RtmClient.createInstance(activity, activity?.getString(R.string.agora_app_id),
                    object : RtmClientListener {
                        override fun onConnectionStateChanged(state: Int, reason: Int) {
                            Log.d(TAG, "Message Connection state changes to "
                                    + state + " reason: " + reason)
                        }

                        override fun onMessageReceived(rtmMessage: RtmMessage, peerId: String) {
                            val msg = rtmMessage.text
                            Log.d(TAG, "Message received  from $peerId$msg"
                            )
                        }

                        override fun onImageMessageReceivedFromPeer(p0: RtmImageMessage?, p1: String?) {
                            TODO("Not yet implemented")
                        }

                        override fun onFileMessageReceivedFromPeer(p0: RtmFileMessage?, p1: String?) {
                            TODO("Not yet implemented")
                        }

                        override fun onMediaUploadingProgress(p0: RtmMediaOperationProgress?, p1: Long) {
                            TODO("Not yet implemented")
                        }

                        override fun onMediaDownloadingProgress(p0: RtmMediaOperationProgress?, p1: Long) {
                            TODO("Not yet implemented")
                        }

                        override fun onTokenExpired() {
                            TODO("Not yet implemented")
                        }

                        override fun onPeersOnlineStatusChanged(p0: MutableMap<String, Int>?) {
                            TODO("Not yet implemented")
                        }
                    })
        } catch (e: java.lang.Exception) {
            throw java.lang.RuntimeException("You need to check the RTM initialization process.")
        }
    }


    fun initializeEngine() {
        mRtcEngine = try {
            Log.d(" RTC Engine ", " RTC Engine ")
            RtcEngine.create(activity, activity?.getString(R.string.agora_app_id), mRtcEventHandler)
        } catch (e: Exception) {
            Log.d(" RTC Engine ", " RTC Engine ${Log.getStackTraceString(e)}")
            throw RuntimeException(
                    """
                    NEED TO check rtc sdk init fatal error
                    ${Log.getStackTraceString(e)}
                    """.trimIndent()
            )
        }
    }

    private fun sendMessageToUser(content: String, userId : String){
        val message = mRtmClient?.createMessage()
        message?.text = content

        val option = SendMessageOptions()
        option.enableOfflineMessaging = true

        mRtmClient?.sendMessageToPeer(userId, message, option, object : ResultCallback<Void?> {
            override fun onSuccess(aVoid: Void?) {

            }

            override fun onFailure(errorInfo: ErrorInfo?) {

            }
        })

    }

    private val mRtmChannelListener: RtmChannelListener = object : RtmChannelListener {
        override fun onMemberCountUpdated(p0: Int) {
            Log.d(" Message From Count ", " Message From Count $p0 ")
        }

        override fun onAttributesUpdated(p0: MutableList<RtmChannelAttribute>?) {

        }

        override fun onMessageReceived(message: RtmMessage, fromMember: RtmChannelMember) {
            val text = message.text
            val fromUser = fromMember.userId

            agoraListener?.messageReceivedOnSuccess(text, fromUser)
            Log.d(" Message From $fromUser", " Message From $text ")
        }

        override fun onImageMessageReceived(p0: RtmImageMessage?, p1: RtmChannelMember?) {
            TODO("Not yet implemented")
        }

        override fun onFileMessageReceived(p0: RtmFileMessage?, p1: RtmChannelMember?) {
            TODO("Not yet implemented")
        }

        override fun onMemberJoined(member: RtmChannelMember) {
            Log.d(" Message From Join ${member.userId}", " Message From Join ${member.channelId} ")
        }
        override fun onMemberLeft(member: RtmChannelMember) {
            Log.d(" Message From Left ${member.userId}", " Message From Left ${member.channelId} ")
        }
    }

    fun sendChannelMessage(msg: String?) {
        val message = mRtmClient?.createMessage()
        message?.text = msg
        mRtmChannel?.sendMessage(message, object : ResultCallback<Void?> {
            override fun onSuccess(aVoid: Void?) {
                msg?.let {
                    agoraListener?.sendMessageOnSuccess(it)
                }
                Log.d(" Message ${aVoid?.toString()}", " Message Success Channel $aVoid")
            }
            override fun onFailure(errorInfo: ErrorInfo?) {
                Log.d(" Message ${errorInfo.toString()}", " Message Fail Channel $errorInfo")
            }
        })
    }

    fun loginWith(username : String){
        mRtmClient?.login(null, username, object : ResultCallback<Void?> {
            override fun onSuccess(responseInfo: Void?) {
                Log.d(" Message ${responseInfo?.toString()}", " Message Success ${username}")
                agoraListener?.loginSuccessful()
            }

            override fun onFailure(errorInfo: ErrorInfo?) {
                Log.d(" Message ${errorInfo.toString()}", " Message Fail ${username}")
                agoraListener?.loginFailure()
            }
        })
    }


}