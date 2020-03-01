package com.mahmoud.carsinsurance.ui.auth.register


import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mahmoud.carsinsurance.R
import com.mahmoud.carsinsurance.Utils.*
import com.mahmoud.carsinsurance.models.GeneralResponse.Status
import com.mahmoud.carsinsurance.models.location.Location
import com.mahmoud.carsinsurance.models.location.SharedViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.input_password
import kotlinx.android.synthetic.main.fragment_register.input_userName

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment(), View.OnClickListener {
    private var navController: NavController? = null
    private var viewModel: RegisterViewModel? = null
    private var locationViewModel: SharedViewModel? = null
    private var role: String? = ""
    private var type: String = ""
    private var location: Location? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        role = arguments?.getString("role")
        locationViewModel = ViewModelProvider(activity!!).get(SharedViewModel::class.java)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setEditText()
        if (role.equals("user")) {
            input_layout_company.visibility = View.GONE
            input_layout_bio.visibility = View.GONE
        }else {
            input_layout_company.visibility = View.VISIBLE
            input_layout_bio.visibility = View.VISIBLE
            val arrayAdapter = ArrayAdapter<String>(context!!,android.R.layout.simple_spinner_item,
                arrayOf("Country","Kuwait","British")
            )
            val onItemSelectedListener: AdapterView.OnItemSelectedListener ?=
                object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (!parent?.getItemAtPosition(position)?.equals("Country")!!)
                        type = parent.getItemAtPosition(position).toString()

                }

            }
            AppUtils.getInstance()?.makeSpinner(arrayAdapter,countrySpinner,0,false
                ,onItemSelectedListener)
        }

        txtLogin.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
        relAddress.setOnClickListener(this)

        locationViewModel?.getLocationLiveData()?.observe(viewLifecycleOwner, Observer { loc ->
            run {
                location = loc
                input_country.setText(loc.address)
            }
        })
        viewModel?.getSource()?.observe(viewLifecycleOwner, Observer { userData ->
            run {
                when (userData.getStatus()) {
                    Status.Failure -> {
                        AppUtils.getInstance()?.dismissDialog()
                        if (userData.getError()?.msg != null)
                            Toast.makeText(
                                context,userData.getError()?.msg, Toast.LENGTH_SHORT).show()
                        else
                            userData.getError()?.throwable?.let {
                                AppUtils.getInstance()?.handleException(context, it)
                            }
                    }
                    Status.Loading -> {
                        AppUtils.getInstance()?.showWaiting(context = context!!)
                    }
                    Status.Success -> {
                        AppUtils.getInstance()?.dismissDialog()
                        SharedPrefManager.getInstance(context)?.setUserData(userData.getData()!!)
                        SharedPrefManager.getInstance(context)?.setLoginStatus(true)
                        Constants.CURRENT_ROLE = userData.getData()?.role
                        openHome(Constants.CURRENT_ROLE)
                    }
                    else -> {}
                }
            }
        })
    }

    private fun openHome(currentRole: String?) {
        when (currentRole) {
            Constants.USER -> navController?.navigate(R.id.action_registerFragment_to_userHomeFragment)
            Constants.COMPANY -> navController?.navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.txtLogin -> navController?.navigate(R.id.action_registerFragment_to_loginFragment)
            R.id.btnRegister -> validate()
            R.id.relAddress ->{
                if(Permissions.getInstance()?.check_Location_Permission(activity!!,this)!!)
                    navController?.navigate(R.id.action_registerFragment_to_mapFragment)}
        }
    }

    private fun validate() {
        val editTexts = arrayOf(input_userName, input_email, input_country, input_password)
        if (!Validator.validateInputField(editTexts, activity!!)) {
            return
        }
        if (!role.equals("user") && type.isEmpty()){
            AppUtils.getInstance()?.makeToast(context,getString(R.string.select_country))
            return
        }
        viewModel?.register(
            name = input_userName.text.toString().trim()
            , email = input_email.text.toString().trim()
            , country = location?.address!!
            , pass = input_password.text.toString().trim()
            , role = role!!
            , type = input_userName.text.toString().trim()
            , details = input_bio.text.toString().trim()
        )
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
            navController?.navigate(R.id.action_registerFragment_to_mapFragment)
        }
    }

    private fun setEditText(){
        input_password.tag = resources.getString(R.string.password)
        input_userName.tag = resources.getString(R.string.userName)
        input_email.tag = resources.getString(R.string.email)
        input_country.tag = resources.getString(R.string.country)
    }

}
