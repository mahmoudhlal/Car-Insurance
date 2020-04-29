package com.mahmoud.carsinsurance.ui.company


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mahmoud.carsinsurance.Injection
import com.mahmoud.carsinsurance.MainActivity
import com.mahmoud.carsinsurance.R
import com.mahmoud.carsinsurance.Utils.SharedPrefManager
import com.mahmoud.carsinsurance.models.requestsResponse.OrdersItem
import com.mahmoud.carsinsurance.ui.user.home.ViewModel.OrderViewModel
import com.mahmoud.carsinsurance.ui.user.home.adapter.OrderAdapter
import kotlinx.android.synthetic.main.fragment_company_home.*
import kotlinx.android.synthetic.main.fragment_user_home.logout

/**
 * A simple [Fragment] subclass.
 */
class CompanyHomeFragment : Fragment() , OrderAdapter.OnItemClickListener,View.OnClickListener {

    companion object{
        var isRefresh : Boolean = false
    }
    private var viewModel: OrderViewModel?=null
    private var navController: NavController?=null
    private var orderAdapter : OrderAdapter?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, Injection.provideOrderViewModelFactory(context = context!!))
            .get(OrderViewModel::class.java)
        viewModel!!.getOrders()
    }

    override fun onResume() {
        super.onResume()
        if (isRefresh){
            viewModel!!.observeOrders()?.value?.dataSource?.invalidate()
            isRefresh=false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_home, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        orderAdapter = OrderAdapter()
        recOrders.adapter=orderAdapter
        recOrders.setHasFixedSize(true)
        viewModel?.observeOrders()?.observe(viewLifecycleOwner, Observer { cPagedList->
            run {
                orderAdapter?.submitList(cPagedList)
            }})

        viewModel?.observeNetwork()?.observe(viewLifecycleOwner, Observer { networkState->
            run {
                orderAdapter?.setNetworkState(networkState)
            }})
        orderAdapter!!.setOnItemClickListener(this)
        logout.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.logout->{
                SharedPrefManager.getInstance(context)?.Logout()
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }
        }
    }

    override fun onItemClick(model: OrdersItem?, position: Int) {
        navController?.navigate(R.id.action_companyHomeFragment_to_orderDetailsFragment,
            bundleOf("order" to model))
    }

}
