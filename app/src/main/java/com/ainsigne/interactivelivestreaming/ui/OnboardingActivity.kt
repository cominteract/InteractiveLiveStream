package com.ainsigne.interactivelivestreaming.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.ainsigne.interactivelivestreaming.R
import com.ainsigne.interactivelivestreaming.databinding.ActivityOnboardingBinding
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.interactivelivestreaming.utils.SharedConfig
import com.ainsigne.interactivelivestreaming.viewmodel.StreamUserViewModel
import kotlinx.android.synthetic.main.activity_onboarding.*
import kotlinx.android.synthetic.main.fragment_onboarding.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class OnboardingActivity : AppCompatActivity() {

    //private val dashboardViewModel by viewModel<StreamDashboardViewModel>()
    //private val detailsViewModel by viewModel<StreamDetailViewModel>()
    private val userViewModel by viewModel<StreamUserViewModel>()
    lateinit var config : SharedConfig

    class OnboardingPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        // Returns total number of pages
        override fun getCount(): Int {
            return NUM_ITEMS
        }

        // Returns the fragment to display for that page
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> OnboardingFragment.newInstance(0)
                1 -> OnboardingFragment.newInstance(1)
                2 -> OnboardingFragment.newInstance(2)
                else -> OnboardingFragment.newInstance(0)
            }
        }

        // Returns the page title for the top indicator
        override fun getPageTitle(position: Int): CharSequence? {
            return "Page $position"
        }

        companion object {
            private const val NUM_ITEMS = 3
        }
    }

    /**
     * checks when already logged by its config it will navigate to the dashboard
     * @param binding as [ActivityOnboardingBinding] to update the views
     */
    private fun checkLogged(binding: ActivityOnboardingBinding) {
        binding.coverOnboarding.visibility = View.VISIBLE
        binding.vpPager.visibility = View.GONE
        config.getUser()?.let {
            userViewModel.getUser(it).observe(this) { user ->
                if (user != null) {
                    navigateWhenAvailable(user)
                }
            }
        }
        if(config.getUser() == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.vpPager.visibility = View.VISIBLE
                binding.coverOnboarding.visibility = View.GONE
            },1500)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityOnboardingBinding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)
        config = SharedConfig(
            getSharedPreferences(application.packageName, Context.MODE_PRIVATE)
        )
        binding.etUsername.setText("user1")

        checkLogged(binding)
        binding.btnStart.setOnClickListener {
            if(!binding.etUsername.text.isNullOrBlank() && binding.etUsername.text.length > 3) {
                userViewModel.getUser(binding.etUsername.text.toString()).observe(this) {
                    if (it != null) {
                        navigateWhenAvailable(it)
                    }else{
                        userViewModel.signupUser(binding.etUsername.text.toString())
                    }
                }
            }
        }
        binding.dot1.setImageResource(R.drawable.white_dot)
        binding.vpPager.adapter = OnboardingPagerAdapter(supportFragmentManager)
        binding.vpPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.dot1.setImageResource(R.drawable.white_dot)
                        binding.dot2.setImageResource(R.drawable.black_dot)
                        binding.dot3.setImageResource(R.drawable.black_dot)

                    }
                    1 -> {
                        binding.dot1.setImageResource(R.drawable.black_dot)
                        binding.dot2.setImageResource(R.drawable.white_dot)
                        binding.dot3.setImageResource(R.drawable.black_dot)
                    }
                    else -> {
                        binding.dot1.setImageResource(R.drawable.black_dot)
                        binding.dot2.setImageResource(R.drawable.black_dot)
                        binding.dot3.setImageResource(R.drawable.white_dot)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    fun navigateWhenAvailable(user : StreamUser?){
        user?.let {
            val username = it.userName
            runOnUiThread {
                config.saveUser(username)
                val i = Intent(
                        this@OnboardingActivity,
                        MainActivity::class.java
                )
                i.putExtra(usernameKey, username)
                startActivity(i)
                finish()
            }
        }
    }

    companion object{
        val usernameKey = "USERNAME"
        val streamTokenKey = "STREAMTOKEN"

    }
}