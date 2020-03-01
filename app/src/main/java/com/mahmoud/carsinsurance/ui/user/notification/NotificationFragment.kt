package com.mahmoud.carsinsurance.ui.user.notification


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mahmoud.carsinsurance.Injection

import com.mahmoud.carsinsurance.R
import com.mahmoud.carsinsurance.ui.user.home.ViewModel.NotificationViewModel
import com.mahmoud.carsinsurance.ui.user.home.adapter.NotificationAdapter
import kotlinx.android.synthetic.main.fragment_notification.*

/**
 * A simple [Fragment] subclass.
 */
class NotificationFragment : Fragment(),View.OnClickListener {
    private var viewModel: NotificationViewModel?=null
    private var navController: NavController?=null
    private var notificationAdapter : NotificationAdapter?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, 
            Injection.provideViewModelFactory(context = context!!))
            .get(NotificationViewModel::class.java)
        viewModel!!.getNotifications()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        notificationAdapter = NotificationAdapter()
        recNotifications.adapter = notificationAdapter
        recNotifications.setHasFixedSize(true)

        viewModel?.observeNotifications()?.observe(viewLifecycleOwner, Observer { cPagedList->
            run {
                notificationAdapter?.submitList(cPagedList)
            }})

        viewModel?.observeNetwork()?.observe(viewLifecycleOwner, Observer { networkState->
            run {
                notificationAdapter?.setNetworkState(networkState)
            }})

    }

    override fun onClick(v: View?) {
        navController?.navigateUp()
    }

}
