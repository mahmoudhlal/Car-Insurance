package com.mahmoud.carsinsurance.fragment.auth.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mahmoud.carsinsurance.R
import com.mahmoud.carsinsurance.Utils.AppUtils
import com.mahmoud.carsinsurance.Utils.Constants
import com.mahmoud.carsinsurance.Utils.SharedPrefManager
import com.mahmoud.carsinsurance.Utils.Validator
import com.mahmoud.carsinsurance.models.GeneralResponse.Status
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() , View.OnClickListener {
    private var navController : NavController ?=null
    private var viewModel : LoginViewModel ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setEditText()
        btnLogin.setOnClickListener(this)
        register.setOnClickListener(this)
        viewModel?.getSource()?.observe(viewLifecycleOwner, Observer { userData->
            run {
                when (userData.getStatus()) {
                    Status.Failure -> {
                        AppUtils.getInstance()?.dismissDialog()
                        if (userData.getError()?.msg != null)
                            Toast.makeText(context, userData.getError()?.msg, Toast.LENGTH_SHORT).show()
                        else
                            userData.getError()?.throwable?.let {AppUtils.getInstance()?.handleException(context, it) }
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
                    else->{}
                }
            }
        } )
    }

    private fun openHome(currentRole: String?){
        when(currentRole){
            Constants.ADMIN->navController?.navigate(R.id.action_loginFragment_to_adminFragment)
            Constants.USER->navController?.navigate(R.id.action_loginFragment_to_userHomeFragment)
            Constants.COMPANY->navController?.navigate(R.id.action_loginFragment_to_companyHomeFragment)
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnLogin->validate()
            R.id.register->navController?.navigate(R.id.action_loginFragment_to_mainFragment)
        }
    }

    private fun validate(){
        val editTexts = arrayOf(input_userName,input_password)
        if (!Validator.validateInputField(editTexts, activity!!)){
            return
        }
        viewModel?.login(email = input_userName.text.toString().trim()
            ,pass = input_password.text.toString().trim())
    }

    private fun setEditText(){
        input_password.tag = resources.getString(R.string.password)
        input_userName.tag = resources.getString(R.string.userName)
    }
}
