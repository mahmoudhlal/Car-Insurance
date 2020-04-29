package com.mahmoud.carsinsurance.ui.user.company.tabs


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmoud.carsinsurance.Injection

import com.mahmoud.carsinsurance.R
import com.mahmoud.carsinsurance.Utils.*
import com.mahmoud.carsinsurance.models.AttachItem
import com.mahmoud.carsinsurance.models.GeneralResponse.Status
import com.mahmoud.carsinsurance.models.PaymentItem
import com.mahmoud.carsinsurance.models.location.Location
import com.mahmoud.carsinsurance.models.location.SharedViewModel
import com.mahmoud.carsinsurance.ui.user.company.CompanyDetailsFragment
import com.mahmoud.carsinsurance.ui.user.company.OpenMapNow
import com.mahmoud.carsinsurance.ui.user.company.Payment.PaymentDialog
import com.mahmoud.carsinsurance.ui.user.company.Payment.PaymentOnClick
import com.mahmoud.carsinsurance.ui.user.company.adapter.ImagesAdapter
import com.mahmoud.carsinsurance.ui.user.home.ViewModel.CompanyViewModel
import com.mahmoud.carsinsurance.ui.user.home.ViewModel.NotificationViewModel
import kotlinx.android.synthetic.main.fragment_insurance_request.btnAddAttachs
import kotlinx.android.synthetic.main.fragment_insurance_request.recAttatchments
import kotlinx.android.synthetic.main.fragment_renew_car.*
import okhttp3.MultipartBody
import java.util.*
import kotlin.math.log

/**
 * A simple [Fragment] subclass.
 */
class RenewCarFragment : Fragment() , View.OnClickListener , ImagesAdapter.OnItemClicked
    , PaymentOnClick {

    private var attachItems: ArrayList<AttachItem>? = null
    private var imagesAdapter: ImagesAdapter? = null
    private var locationViewModel: SharedViewModel? = null
    private var companyViewModel: CompanyViewModel? = null
    private var location: Location? = null
    private var Id: Int? = null
    private var paymentItem: PaymentItem? = null
    private var paymentDialog: PaymentDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Id = arguments?.getInt("id")
        locationViewModel = ViewModelProvider(activity!!).get(SharedViewModel::class.java)
        companyViewModel =
            ViewModelProvider(this, Injection.provideViewModelFactory(context = context!!))
                .get(CompanyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renew_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        locationViewModel!!.getLocationLiveData()?.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                run {
                    location=it
                    edtAddress.setText(location!!.address)
                }
            })
    }


    private fun initView(){
        btnAddAttachs.setOnClickListener(this)
        btnReSave.setOnClickListener(this)
        relAddress.setOnClickListener(this)
        relFeees.setOnClickListener(this)

        ////add attaches
        recAttatchments.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, false
        )
        recAttatchments.setHasFixedSize(true)
        attachItems = ArrayList()
        imagesAdapter = ImagesAdapter(attachItems)
        recAttatchments.adapter = imagesAdapter
        imagesAdapter!!.setOnItemClickListener(this)

        companyViewModel?.observeRenewOrder()?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            kotlin.run {
                when(it.getStatus()){
                    Status.Loading->{
                        AppUtils.getInstance()?.showWaiting(context = context!!)
                    }
                    Status.Failure->{
                        AppUtils.getInstance()?.dismissDialog()
                        Toast.makeText(context,it.getError()?.msg,Toast.LENGTH_SHORT).show()
                    }
                    Status.Success->{
                        AppUtils.getInstance()?.dismissDialog()
                        Toast.makeText(context,"Your request sent successfully wait for company approve",Toast.LENGTH_SHORT).show()
                        CompanyDetailsFragment.navUp()
                    }
                    else -> {}
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAddAttachs ->{
                TakePhotoUtils.getInstance()?.SelectPhotoDialog(activity!!,this)
            }
            R.id.btnReSave ->{validate()}
            R.id.relAddress ->{
                openMapNow?.openMap()}
            R.id.relFeees ->{
                inflatePaymentDialog()}
        }
    }


    private fun validate(){

        if (attachItems?.size == 0){
            Toast.makeText(context,"Add Car Images!", Toast.LENGTH_SHORT).show()
            return
        }

        if (edtAddress.text.toString().trim().isEmpty()){
            edtAddress.error = "Required Field !"
            return
        }

        if (edtFeees.text.toString().trim().isEmpty()){
            edtFeees.error = "Required Field !"
            return
        }

        val uploadFilesTemp: MutableList<MultipartBody.Part?> =
            ArrayList()
        for (i in attachItems!!.indices) {
            when (attachItems!![i].type) {
                "image" -> uploadFilesTemp.add(
                    TakePhotoUtils.getInstance()!!.getMultiPartFromBitmap(
                        attachItems!![i].bitmap!!,
                        activity!!,
                        "attachs[]"
                    )
                )
            }
        }
        companyViewModel?.addOrder(
            SharedPrefManager.getInstance(context = context)?.getToken(),Id,"renew"
            ,location?.address,location?.lat.toString(),location?.lng.toString(),
            paymentItem?.cardNum,
            paymentItem?.eDate,
            paymentItem?.name,
            paymentItem?.money,
            paymentItem?.cvv,
            uploadFilesTemp.toTypedArray())
    }

    override fun showItem(isAdd: Boolean, position: Int) {
    }

    override fun removeItem(position: Int) {
        imagesAdapter?.removeItem(position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.CAMERA) {
                assert(data != null)
                //attachItems.add();
                attachItems?.add(
                    AttachItem(
                        "image",
                        TakePhotoUtils.getInstance()!!.getBitmap(
                            Constants.CAMERA, data!!.data!!,
                            activity!!
                        )
                    )
                )
            } else if (requestCode == Constants.GALLERY) {
                assert(data != null)
                //attachItems.add();
                attachItems?.add(
                    AttachItem(
                        "image",
                        TakePhotoUtils.getInstance()!!.getBitmap(
                            Constants.GALLERY, data!!.data!!,
                            activity!!
                        )
                    )
                )
            }
            imagesAdapter?.notifyDataSetChanged()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.permission_camera_code) {
            for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission == Manifest.permission.CAMERA) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        TakePhotoUtils.getInstance()?.ChooseImageCamera(activity!!, this)
                        break
                    }
                }
            }
        } else if (requestCode == Constants.permission_write_data) {
            for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        TakePhotoUtils.getInstance()?.ChooseImageGallery(activity!!, this)
                        break
                    }
                }
            }
        }
    }

    companion object{
        private var openMapNow : OpenMapNow? = null
        fun openMap(openMapNow:OpenMapNow){
            this.openMapNow=openMapNow
        }
    }

    private fun inflatePaymentDialog() {
        paymentDialog = PaymentDialog()
        paymentDialog?.show(childFragmentManager, "")
        paymentDialog?.isCancelable = true
        paymentDialog?.setOnClickView(this)
    }

    override fun onClick(
        cardNum: String?,
        eDate: String?,
        name: String?,
        money: String?,
        cvv: String?
    ) {
        Log.d("OOO",cardNum!!)
        paymentItem = PaymentItem(cardNum, eDate, name, money, cvv)
        edtFeees.setText("Your card information will be reviewed")
        paymentDialog?.dismiss()

    }
}
