package com.ainsigne.interactivelivestreaming.ui

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ainsigne.interactivelivestreaming.R
import com.ainsigne.interactivelivestreaming.databinding.StreamDetailFragmentBinding
import com.ainsigne.interactivelivestreaming.interfaces.AgoraListener
import com.ainsigne.interactivelivestreaming.interfaces.BackListener
import com.ainsigne.interactivelivestreaming.model.AgoraClient
import com.ainsigne.interactivelivestreaming.model.StreamChat
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.interactivelivestreaming.model.Streams
import com.ainsigne.interactivelivestreaming.viewmodel.StreamItemDetailViewModel
import com.ainsigne.interactivelivestreaming.viewmodel.StreamItemViewModel
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import com.google.android.material.snackbar.Snackbar
import io.agora.rtc.RtcEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.bind
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class StreamDetailFragment : Fragment() , AgoraListener{

    companion object {
        fun newInstance() = StreamDetailFragment()
    }

    private val PERMISSION_REQ_ID = 22
    private val REQUESTED_PERMISSIONS = arrayOf<String>(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val args: StreamDetailFragmentArgs by navArgs()
    var token : String? = null
    var username : String? = null
    var userId : String? = null
    var channel : String? = null
    var userToWatch : StreamUser? = null
    var userToJoin : StreamUser? = null
    var visitorname : String? = null
    var adapter : StreamChatAdapter? = null
    var imgB64 : String? = null
    var agoraClient = AgoraClient()

    lateinit var binding: StreamDetailFragmentBinding

    private val detailViewModel by viewModel<StreamItemDetailViewModel>()
    private val dashboardViewModel by viewModel<StreamItemViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =  DataBindingUtil.inflate<StreamDetailFragmentBinding>(
            inflater, R.layout.stream_detail_fragment, container, false
        )

        context ?: return binding.root
        adapter = StreamChatAdapter()
        binding.rvChats.adapter = adapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        agoraClient.setAgoraListener(this)
        activity?.let {
            agoraClient.setContext(it)
        }


        channel = args.channelId
        visitorname = args.userToJoin
        username = args.userToWatch
        userId = args.userId
        channel?.let { channell ->
            detailViewModel.getChatsFromChannel(channell).observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    adapter?.submitList(it)
                }
            }
        }
        if(args.status) {

            agoraClient.initializeEngine()
            agoraClient.initializeChat()
            username?.let { userName ->
                adapter?.updateStreamer(userName)
                if (username != visitorname)
                    binding.tvViewingChannel.setText(
                        getString(
                            R.string.you_are_watching,
                            "${username}"
                        )
                )
                detailViewModel.getCurrentUser(userName).observe(viewLifecycleOwner) { userToW ->
                    userToWatch = userToW
                }
            }
            visitorname?.let {
                agoraClient.loginWith(it)
                binding.tvSendingAsUser.setText(
                    getString(
                        R.string.you_are_sending,
                        "${it}"
                    )
                )
                detailViewModel.getCurrentUser(it).observe(viewLifecycleOwner) { userToJ ->
                    userToJoin = userToJ
                }
            }

            if (isGranted()) {
                startLive(binding.flContainer)
            } else {
                requestPermissions(REQUESTED_PERMISSIONS, PERMISSION_REQ_ID)
            }
        }else{
            binding.btnSend.visibility = View.GONE
            binding.etChat.visibility = View.GONE
            binding.tvLiveFinish?.visibility = View.VISIBLE
            binding.coverOnboarding?.visibility = View.VISIBLE
        }

    }

    /**
     * check if permissions are granted
     */
    fun isGranted() : Boolean {
        return (activity?.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                activity?.checkSelfPermission(Manifest.permission.CAMERA)  == PackageManager.PERMISSION_GRANTED &&
                activity?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    /**
     * start live whether joining remotely or starting a local stream
     * @param frameLayout as [FrameLayout] unused
     */
    private fun startLive(frameLayout: FrameLayout){
        username?.let {
            if(!userId.isNullOrEmpty()){
                agoraClient.setupRemoteVideo(Integer.valueOf(userId), binding.flContainer)
            }else{
                agoraClient.setupLocalVideo(binding.flContainer)
            }
            agoraClient.setChannelProfile()

            //setupRemoteVideo(12345)
            channel?.let { channel ->
                agoraClient.joinChannel(token, channel)
            }
        }
    }

    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (activity?.let { ContextCompat.checkSelfPermission(it, permission) } !=
                PackageManager.PERMISSION_GRANTED
        ) {
            activity?.let { ActivityCompat.requestPermissions(
                it,
                REQUESTED_PERMISSIONS,
                requestCode
            ) }
            return false
        }
        return true
    }

    private var previousOrientation = Configuration.ORIENTATION_UNDEFINED
    private var rotating = false

    private fun getOrientationAsString(orientation: Int): String {
        return if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            "Landscape"
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            "Portrait"
        } else "Undefined"
    }

    private fun debugDescribeOrientations(currentOrientation: Int) {
        Log.v("Orientation", "previousOrientation: " + getOrientationAsString(previousOrientation))
        Log.v("Orientation", "currentOrientation: " + getOrientationAsString(currentOrientation))
    }

    private fun checkAndSetOrientationInfo() {
        val currentOrientation = resources.configuration.orientation
        debugDescribeOrientations(currentOrientation)
        if (previousOrientation !== android.content.res.Configuration.ORIENTATION_UNDEFINED // starts undefined
                && previousOrientation !== currentOrientation) rotating = true
        previousOrientation = currentOrientation
    }

    override fun onPause() {
        super.onPause()
        if (requireActivity().isFinishing) {
            Log.v("onPause", "Finishing")
        } else {
            checkAndSetOrientationInfo();
        }
    }

    /**
     * adds delays before leaving the channel and destroying the engine
     * @param listener from the main activity which determines when the view will be destroyed from back pressed
     */
    fun removeAll(listener : BackListener?){
        if(args.status) {
            binding.flContainer.visibility = View.GONE
            binding.coverOnboarding?.visibility = View.VISIBLE
            if(visitorname == username)
                binding.tvLiveFinish?.visibility = View.VISIBLE
            binding.tvSendingAsUser.visibility = View.GONE
            binding.tvViewingChannel.visibility = View.GONE
            binding.ivStreamImagePic.visibility = View.GONE
            binding.animationView.visibility = View.GONE
            binding.btnSend.visibility = View.GONE
            binding.etChat.visibility = View.GONE
            createStream(false)
            agoraClient.leaveChannel()
            Handler(Looper.getMainLooper()).postDelayed({
                RtcEngine.destroy()
                if (!rotating)
                    agoraClient.emptyObjects()

            }, 3000)
            Handler(Looper.getMainLooper()).postDelayed({
                listener?.onClose()
            }, 4000)
        }else{
            listener?.onClose()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * takes an image preview from the surface of the running stream
     * @param surfaceView as [SurfaceView] the view to take a shot of
     */
    private fun takePhoto(surfaceView: SurfaceView) {

        // Create a bitmap the size of the scene view.
        val bitmap = Bitmap.createBitmap(
            surfaceView.getWidth(), surfaceView.getHeight(),
            Bitmap.Config.ARGB_8888
        )

        // Create a handler thread to offload the processing of the image.
        val handlerThread = HandlerThread("PixelCopier")
        handlerThread.start()
        // Make the request to copy.

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            PixelCopy.request(surfaceView, bitmap, { copyResult ->
                if (copyResult == PixelCopy.SUCCESS) {
                    activity?.runOnUiThread {
                        binding.ivStreamImagePic.setImageBitmap(bitmap)
                    }
                    imgB64 = Streams.BitmapToBase64(bitmap)
                    createStream(true)
                }
                handlerThread.quitSafely()
            }, Handler(handlerThread.looper))
        }
    }

    /**
     * creates a stream to be posted on server and cached for offline purpose
     * @param status as [Boolean] the status to identify the stream whether live or not
     */
    private fun createStream(status: Boolean){
        channel?.let { channel ->
            userToWatch?.let { user ->
                userId?.let { userId ->
                    val streamModel = Streams(
                        channel, user, Date().toStringFormat(),
                        status, imgB64, Integer.valueOf(userId)
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        if(!dashboardViewModel.isStreamCreated) {
                            dashboardViewModel.createStream(streamModel)
                        }else{
                            dashboardViewModel.updateStream(streamModel)
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(isGranted() && requestCode == PERMISSION_REQ_ID){
            startLive(binding.flContainer)
        }
    }
    /**
     * when joining a channel is successful
     * @param channell as [String] the channel to identify which stream is joined
     */
    override fun onJoinChannelSuccess(channell: String, uid: Int, elapsed: Int) {
        userId = uid.toString()
    }

    class SurfaceCallback : SurfaceHolder.Callback {
        var isSurfaceCreated = false
        override fun surfaceChanged(
            holder: SurfaceHolder, format: Int,
            width: Int, height: Int
        ) {
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            // you need to start your drawing thread here

            isSurfaceCreated = true

        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            // and here you need to stop it
            isSurfaceCreated = false

        }
    }
    /**
     * when streaming was successful on client side
     * @param unused
     */
    override fun onFirstRemoteVideoFrame(width: Int, height: Int, elapsed: Int) {

    }
    /**
     * when streaming was successful on host side
     * it can now save a preview image and create stream to be posted to the server
     * @param unused
     */
    override fun onFirstLocalVideoFrame(width: Int, height: Int, elapsed: Int) {
        binding.flContainer.let {
            val surfaceView = binding.flContainer.get(0) as SurfaceView
            surfaceView.holder.addCallback(SurfaceCallback())
            Handler(Looper.getMainLooper()).postDelayed({
                if (surfaceView.holder.surface.isValid)
                    takePhoto(surfaceView)
                else if (imgB64 != null)
                    createStream(true)
                binding.animationView.visibility = View.VISIBLE
                visitorname?.let { visitorname ->
                    username?.let { username ->
                        if(username == visitorname) {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.you_are_live),
                                Snackbar.LENGTH_LONG
                            )
                                .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
                                .show()
                        }
                    }
                }
            }, 5000)
        }
    }
    /**
     * when sending message to the channel is successful
     * it can now be saved locally
     * @param msg as [String] the msg that was sent
     */
    override fun sendMessageOnSuccess(msg: String) {
        channel?.let { channell ->
            msg.let { msge ->
                visitorname?.let{ visitor ->
                    val streamChat = StreamChat(
                        UUID.randomUUID().toString(), msge, channell,
                        visitor, Date().toStringFormat()
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        detailViewModel.createChat(streamChat)
                    }
                }
            }
        }
    }
    /**
     * when another user sending message to the channel is successful
     * it can now be saved locally
     * @param text as [String] the msg that was sent
     * @param fromUser as [String] then user that sent the message
     */
    override fun messageReceivedOnSuccess(text: String, fromUser: String) {
        channel?.let { channell ->
            val streamChat = StreamChat(
                UUID.randomUUID().toString(), text, channell,
                fromUser, Date().toStringFormat()
            )
            CoroutineScope(Dispatchers.IO).launch {
                detailViewModel.createChat(streamChat)
            }
        }
    }
    /**
     * when login is successful and can now send realtime message
     */
    override fun loginSuccessful() {
        activity?.runOnUiThread {
            binding.btnSend.visibility = View.VISIBLE
            binding.btnSend.setOnClickListener {
                //sendMessageToUser(et_chat.text.toString())
                agoraClient.sendChannelMessage(binding.etChat.text.toString())
            }
        }
    }
    /**
     * when login fails
     */
    override fun loginFailure() {
        activity?.runOnUiThread {
            binding.btnSend.visibility = View.GONE
        }
    }
    /**
     * when host streamer has left notify that stream is finished
     */
    override fun onHostLeft() {
        if(visitorname != username) {
            activity?.runOnUiThread {
                binding.flContainer.visibility = View.GONE
                binding.coverOnboarding?.visibility = View.VISIBLE
                binding.tvLiveFinish?.visibility = View.VISIBLE
                binding.tvSendingAsUser.visibility = View.GONE
                binding.tvViewingChannel.visibility = View.GONE
                binding.ivStreamImagePic.visibility = View.GONE
                binding.animationView.visibility = View.GONE
                binding.btnSend.visibility = View.GONE
                binding.etChat.visibility = View.GONE
            }
        }

    }
}