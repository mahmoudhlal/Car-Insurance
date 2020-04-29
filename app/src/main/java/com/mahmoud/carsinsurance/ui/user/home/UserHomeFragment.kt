package com.mahmoud.carsinsurance.ui.user.home


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mahmoud.carsinsurance.Injection
import com.mahmoud.carsinsurance.MainActivity
import com.mahmoud.carsinsurance.R
import com.mahmoud.carsinsurance.Utils.AppUtils
import com.mahmoud.carsinsurance.Utils.Constants
import com.mahmoud.carsinsurance.Utils.SharedPrefManager
import com.mahmoud.carsinsurance.models.GeneralResponse.Status
import com.mahmoud.carsinsurance.models.companiesResponse.CompaniesItem
import com.mahmoud.carsinsurance.ui.user.home.ViewModel.CompanyViewModel
import com.mahmoud.carsinsurance.ui.user.home.adapter.CompanyAdapter
import kotlinx.android.synthetic.main.fragment_user_home.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class UserHomeFragment : Fragment() , CompanyAdapter.OnItemClickListener,View.OnClickListener{

    private var viewModel: CompanyViewModel?=null
    private var navController:NavController ?=null
    private var companyAdapter : CompanyAdapter ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,Injection.provideViewModelFactory(context = context!!))
            .get(CompanyViewModel::class.java)
        viewModel!!.getCompanies()

    }

    override fun onResume() {
        super.onResume()
        viewModel!!.getNotificationCount(SharedPrefManager.getInstance(context)?.getToken())
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
        if (Constants.CURRENT_ROLE.equals("admin")){
            companyAdapter!!.setIsAdmin(true)
            messages.visibility = View.GONE
        }
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
        companyAdapter!!.setOnItemClickListener(this)
        logout.setOnClickListener(this)
        messages.setOnClickListener(this)

        viewModel?.observeApproval()?.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it.getStatus()) {
                    Status.Loading -> {
                        AppUtils.getInstance()?.showWaiting(context = context!!)
                    }
                    Status.Failure -> {
                        AppUtils.getInstance()?.dismissDialog()
                        Toast.makeText(context,it.getError()?.msg, Toast.LENGTH_SHORT).show()
                    }
                    Status.Success -> {
                        AppUtils.getInstance()?.dismissDialog()
                        Toast.makeText(context,"successfully",
                            Toast.LENGTH_SHORT).show()
                        viewModel!!.observeCompanies()?.value
                            ?.dataSource?.invalidate()
                    }
                    else -> {
                    }
                }
            })

        viewModel?.noCount?.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it.getStatus()) {
                    Status.Loading -> {
                    }
                    Status.Failure -> {
                        Toast.makeText(context,it.getError()?.msg, Toast.LENGTH_SHORT).show()
                    }
                    Status.Success -> {
                       if (it.getData()?.data!! > 0){
                           cNot.visibility = View.VISIBLE
                       }else{
                           cNot.visibility = View.GONE
                       }
                    }
                    else -> {
                    }
                }
            })

    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.logout->{
                SharedPrefManager.getInstance(context)?.Logout()
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }
            R.id.messages->{
                navController?.navigate(R.id.action_userHomeFragment_to_notificationFragment)
            }
        }
    }

    override fun onItemClick(model: CompaniesItem?, position: Int) {
        navController?.navigate(R.id.action_userHomeFragment_to_companyDetailsFragment
            , bundleOf("id" to model!!.id))    }

    override fun onItemApproveClick(model: CompaniesItem?, position: Int) {
        viewModel?.approveToCompanyRequest(SharedPrefManager.getInstance(context)?.getToken(),model?.id,1)
    }

    override fun onItemRefuseClick(model: CompaniesItem?, position: Int) {
        viewModel?.approveToCompanyRequest(SharedPrefManager.getInstance(context)?.getToken(),model?.id,0)
    }
}
