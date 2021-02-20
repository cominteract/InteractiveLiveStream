package com.ainsigne.interactivelivestreaming.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.ainsigne.interactivelivestreaming.R
import com.ainsigne.interactivelivestreaming.databinding.StreamDashboardFragmentBinding
import com.ainsigne.interactivelivestreaming.utils.SharedConfig
import com.ainsigne.interactivelivestreaming.viewmodel.StreamItemViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class StreamDashboardFragment : Fragment() {

    companion object {
        fun newInstance() = StreamDashboardFragment()
    }

    var adapter : StreamsAdapter? = null
    private val dashboardViewModel by viewModel<StreamItemViewModel>()
    lateinit var config : SharedConfig
    lateinit var binding: StreamDashboardFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.getSharedPreferences(activity?.application?.packageName, Context.MODE_PRIVATE)?.let {
            config = SharedConfig(it)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding =  DataBindingUtil.inflate<StreamDashboardFragmentBinding>(
                inflater, R.layout.stream_dashboard_fragment, container, false)
        context ?: return binding?.root
        val username = activity?.intent?.extras?.getString(OnboardingActivity.usernameKey)
        adapter = StreamsAdapter()
        username?.let { it ->
            adapter?.setupVisitor(it)
        }
        binding.rvStreams.adapter = adapter
        binding.btnLogout.setOnClickListener {
            config.logoutUser()
            val i = Intent(
                    activity,
                    OnboardingActivity::class.java
            )
            startActivity(i)
            activity?.finish()
        }
        binding.btnLive.setOnClickListener {
            val channel = UUID.randomUUID().toString()
            username?.let { username ->

                val direction = StreamDashboardFragmentDirections.actionStreamDashboardToStreamDetail(
                        channel, username, username, "", true
                )
                it.findNavController().navigate(direction)

            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.coverOnboarding.visibility = View.VISIBLE
        dashboardViewModel.getAllStreamsCache().observe(viewLifecycleOwner){
            if (!it.isNullOrEmpty()) {
                adapter?.submitList(it)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.rvStreams.smoothScrollToPosition(0)
                }, 1000)

                binding.coverOnboarding.visibility = View.GONE
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
                binding.coverOnboarding.visibility = View.GONE
        }, 1000)
        dashboardViewModel.getAllStreams()
        binding.pullToRefresh.setOnRefreshListener(OnRefreshListener {
            dashboardViewModel.getAllStreams() // your code
            binding.pullToRefresh.isRefreshing = false
        })
    }
}