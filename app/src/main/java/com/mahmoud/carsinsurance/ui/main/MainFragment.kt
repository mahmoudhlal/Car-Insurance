package com.mahmoud.carsinsurance.ui.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.mahmoud.carsinsurance.R
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() , View.OnClickListener {

    private var  navController:NavController?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        btnCustomer.setOnClickListener(this)
        btnCompany.setOnClickListener(this)
        txtLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnCompany->{
                navController?.navigate(R.id.action_mainFragment_to_registerFragment
                    ,bundleOf("role" to "company"))}
            R.id.btnCustomer->{navController?.navigate(R.id.action_mainFragment_to_registerFragment
                ,bundleOf("role" to "user"))}
            R.id.txtLogin->{navController?.navigate(R.id.action_mainFragment_to_loginFragment)}
        }
    }


}
