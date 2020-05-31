package com.mahmoud.carsinsurance.ui.user.company


import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.mahmoud.carsinsurance.R
import com.mahmoud.carsinsurance.Utils.Constants
import com.mahmoud.carsinsurance.Utils.Permissions
import com.mahmoud.carsinsurance.ui.user.company.adapter.OffersAdapter
import com.mahmoud.carsinsurance.ui.user.company.adapter.TabAdapter
import com.mahmoud.carsinsurance.ui.user.company.tabs.InsuranceRequestFragment
import com.mahmoud.carsinsurance.ui.user.company.tabs.RenewCarFragment
import kotlinx.android.synthetic.main.fragment_company_details.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CompanyDetailsFragment : Fragment() , View.OnClickListener , OpenMapNow {

    private var Id : Int ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Id = arguments?.getInt("id")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setViewPager()
    }

    private fun setViewPager(){
        btnBack.setOnClickListener(this)
        val offersAdapter = OffersAdapter()
        viewPager.adapter = offersAdapter
        offersAdapter.updateList(Constants.offersImages.shuffled())

        val insuranceType = arrayOf("Insurance Request","Renew Car")
        val tabAdapter = TabAdapter(activity!!)
        val insuranceRequestFragment = InsuranceRequestFragment()
        val renewCarFragment = RenewCarFragment()
        insuranceRequestFragment.arguments = bundleOf("id" to Id)
        renewCarFragment.arguments = bundleOf("id" to Id)
        arrayListOf(insuranceRequestFragment, renewCarFragment)
        tabAdapter.putFragmentList(arrayListOf(insuranceRequestFragment, renewCarFragment))
        viewPager2.adapter = tabAdapter
        TabLayoutMediator(
            tabLayout, viewPager2,
            TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                tab.text = insuranceType[position]
            }
        ).attach()
        RenewCarFragment.openMap(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnBack ->{
                navController?.navigateUp()
            }
        }
    }

    override fun openMap() {
        if(Permissions.getInstance()?.check_Location_Permission(activity!!,this)!!)
            navController?.navigate(R.id.action_companyDetailsFragment_to_mapFragment)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.PERMISSION_ACCESS_LOCATION) {
            for (grantResult in grantResults) if (grantResult == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.you_must_give_permissions_location),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            navController?.navigate(R.id.action_companyDetailsFragment_to_mapFragment)
        }
    }

    companion object{
        private var navController :NavController ? = null
        fun navUp(){
         navController?.navigateUp()
        }
    }

}
