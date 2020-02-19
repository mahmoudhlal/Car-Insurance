package com.mahmoud.carsinsurance.fragment.splash


import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mahmoud.carsinsurance.R
import com.mahmoud.carsinsurance.Utils.Constants
import com.mahmoud.carsinsurance.Utils.SharedPrefManager
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : Fragment() {
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onPause() {
        super.onPause()
        activity?.window
            ?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        loading()
    }

    private fun loading() {
        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                if (SharedPrefManager.getInstance(context)!!.getLoginStatus())
                    Constants.CURRENT_ROLE?.let { openHomeActivity(it)}
                else openLogin()
            }
        }.start()
    }


    private fun openLogin() {
        navController?.navigate(R.id.action_splashFragment_to_mainFragment)
    }


    private fun openHomeActivity(role: String) {
        when (role) {
            Constants.ADMIN -> navController?.navigate(R.id.action_splashFragment_to_adminFragment)
            Constants.USER -> navController?.navigate(R.id.action_splashFragment_to_userHomeFragment)
            Constants.COMPANY -> navController?.navigate(R.id.action_splashFragment_to_companyHomeFragment)
        }
    }


}
