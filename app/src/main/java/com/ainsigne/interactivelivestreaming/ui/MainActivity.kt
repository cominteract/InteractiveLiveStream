package com.ainsigne.interactivelivestreaming.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.ainsigne.interactivelivestreaming.R
import com.ainsigne.interactivelivestreaming.databinding.ActivityMainBinding
import com.ainsigne.interactivelivestreaming.interfaces.BackListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.stream_detail_fragment.*
import java.util.*


class MainActivity : AppCompatActivity(), BackListener {
    private lateinit var navController: NavController
    private var isGoingBack = false
    override fun onCreate(savedInstanceState: Bundle?) {
        //streamRepository = StreamItemRepository()

        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.stream_nav_fragment)

    }

    /**
     * Quite a hack to prevent crash from leaving the channel without any listener.
     * null upcall object in agora::rtm::IChannelEventHandler::onLeave
     */
    override fun onBackPressed() {
        if (navController.currentBackStackEntry?.destination?.id == R.id.stream_detail_fragment && !isGoingBack) {
            isGoingBack = true
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.stream_nav_fragment) as NavHostFragment?
            val fragment: StreamDetailFragment = navHostFragment?.childFragmentManager?.primaryNavigationFragment as StreamDetailFragment
            fragment.removeAll(this)
        }else if(!isGoingBack){
            super.onBackPressed()
        }
    }

    override fun onClose() {
        isGoingBack = false
        super.onBackPressed()
    }
}