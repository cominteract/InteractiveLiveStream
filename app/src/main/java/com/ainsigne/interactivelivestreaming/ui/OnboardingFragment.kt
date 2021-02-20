package com.ainsigne.interactivelivestreaming.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.ainsigne.interactivelivestreaming.R
import kotlinx.android.synthetic.main.fragment_onboarding.*


/**
 * A simple [Fragment] subclass.
 * Use the [OnboardingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnboardingFragment : Fragment() {
    val positionKey = "VIEW_PAGER_POSITION"
    var position = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(positionKey)
            if(position == 0) {

                iv_onboarding_image.load(R.drawable.onboarding_first) {
                    crossfade(true)
                    placeholder(R.drawable.onboarding_first)
                }


            } else if(position == 1) {
                iv_onboarding_image.load(R.drawable.onboarding_second) {
                    crossfade(true)
                    placeholder(R.drawable.onboarding_first)
                }
            }else {
                iv_onboarding_image.load(R.drawable.onboarding_third) {
                    crossfade(true)
                    placeholder(R.drawable.onboarding_first)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OnboardingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int) =
            OnboardingFragment().apply {
                arguments = Bundle().apply {
                    putInt(positionKey, param1)
                }
            }
    }
}