package com.mahmoud.carsinsurance.fragment.user.home


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mahmoud.carsinsurance.Injection
import com.mahmoud.carsinsurance.R
import com.mahmoud.carsinsurance.fragment.user.home.ViewModel.CompanyViewModel
import com.mahmoud.carsinsurance.fragment.user.home.adapter.CompanyAdapter
import kotlinx.android.synthetic.main.fragment_user_home.*

/**
 * A simple [Fragment] subclass.
 */
class UserHomeFragment : Fragment() {

    private var viewModel:CompanyViewModel?=null
    private var navController:NavController ?=null
    private var companyAdapter : CompanyAdapter ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,Injection.provideViewModelFactory(context = context!!))
            .get(CompanyViewModel::class.java)
        viewModel!!.getCompanies()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        companyAdapter = CompanyAdapter()
        recCompanies.adapter=companyAdapter
        recCompanies.setHasFixedSize(true)

        viewModel?.observeCompanies()?.observe(viewLifecycleOwner, Observer { cPagedList->
            run {
                companyAdapter?.submitList(cPagedList)
            }})

        viewModel?.observeNetwork()?.observe(viewLifecycleOwner, Observer { networkState->
            run {
                companyAdapter?.setNetworkState(networkState)
            }})


    }

}
