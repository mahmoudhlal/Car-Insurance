package com.mahmoud.carsinsurance.ui.user.company.tabs


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmoud.carsinsurance.Injection
import com.mahmoud.carsinsurance.R
import com.mahmoud.carsinsurance.Utils.AppUtils
import com.mahmoud.carsinsurance.Utils.Constants
import com.mahmoud.carsinsurance.Utils.SharedPrefManager
import com.mahmoud.carsinsurance.Utils.TakePhotoUtils
import com.mahmoud.carsinsurance.models.AttachItem
import com.mahmoud.carsinsurance.models.GeneralResponse.Status
import com.mahmoud.carsinsurance.models.PaymentItem
import com.mahmoud.carsinsurance.models.location.Location
import com.mahmoud.carsinsurance.models.location.SharedViewModel
import com.mahmoud.carsinsurance.ui.company.SendMessage.SendMessageDialog
import com.mahmoud.carsinsurance.ui.user.company.CompanyDetailsFragment
import com.mahmoud.carsinsurance.ui.user.company.Payment.PaymentDialog
import com.mahmoud.carsinsurance.ui.user.company.Payment.PaymentOnClick
import com.mahmoud.carsinsurance.ui.user.company.adapter.ImagesAdapter
import com.mahmoud.carsinsurance.ui.user.home.ViewModel.CompanyViewModel
import com.mahmoud.carsinsurance.ui.user.home.ViewModel.NotificationViewModel
import kotlinx.android.synthetic.main.fragment_insurance_request.*
import okhttp3.MultipartBody
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class InsuranceRequestFragment : Fragment(), View.OnClickListener, ImagesAdapter.OnItemClicked,
    TextWatcher , PaymentOnClick {

    private var attachItems: ArrayList<AttachItem>? = null
    private var imagesAdapter: ImagesAdapter? = null
    private var locationViewModel: SharedViewModel? = null
    private var companyViewModel: CompanyViewModel? = null
    private var location: Location? = null
    private var carType: String? = null
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

    private var INSURANCE_TYPE: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insurance_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }


    private fun initView() {
        btnAddAttachs.setOnClickListener(this)
        relDiamond.setOnClickListener(this)
        relGolden.setOnClickListener(this)
        relSilver.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        relFees.setOnClickListener(this)

        val arrayAdapter = ArrayAdapter<String>(
            context!!, android.R.layout.simple_spinner_item,
            arrayOf("Car Type", "Salon", "Geep", "Sport")
        )
        val onItemSelectedListener: AdapterView.OnItemSelectedListener? =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (!parent?.getItemAtPosition(position)?.equals("Car Type")!!)
                        carType = parent.getItemAtPosition(position).toString()

                }

            }
        AppUtils.getInstance()?.makeSpinner(
            arrayAdapter, countrySpinner, 0, false
            , onItemSelectedListener
        )

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

        edtCarModel.addTextChangedListener(this)

        companyViewModel?.observeAddOrder()?.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it.getStatus()) {
                    Status.Loading -> {
                        AppUtils.getInstance()?.showWaiting(context = context!!)
                    }
                    Status.Failure -> {
                        AppUtils.getInstance()?.dismissDialog()
                        Toast.makeText(context, it.getError()?.msg, Toast.LENGTH_SHORT).show()
                    }
                    Status.Success -> {
                        AppUtils.getInstance()?.dismissDialog()
                        Toast.makeText(
                            context,
                            "Your request sent successfully wait for company approve",
                            Toast.LENGTH_SHORT
                        ).show()
                        CompanyDetailsFragment.navUp()
                    }
                    else -> {
                    }
                }
            })

        autoRadio.setOnCheckedChangeListener{btn , isChecked ->
            if (isChecked){
                if (carType == null || edtCarModel.text.toString().isEmpty()
                    || edtAge.text.toString().isEmpty() || edtNumOfAccident.text.toString().isEmpty()) {
                    radioGroup.clearCheck()
                    Toast.makeText(
                        context,
                        "Select car type and car model and number of accident and your age first!",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{

                    if (edtAge.text.toString().toInt() < 40
                        && Integer.valueOf(edtCarModel.text.toString().trim()) > Calendar.getInstance().get(Calendar.YEAR) - 5
                        && edtNumOfAccident.text.toString().toInt() == 0
                        && carType.equals("Sport")
                            ){
                        INSURANCE_TYPE = "diamond"
                        imgDiamond.setImageDrawable(resources.getDrawable(R.drawable.ic_tick))
                        imgGold.setImageDrawable(null)
                        imgSilver.setImageDrawable(null)
                        txtDetails.visibility = View.VISIBLE
                        txtDetails.text = "diamond insurance"
                    }else if(edtAge.text.toString().toInt() < 40
                        && Integer.valueOf(edtCarModel.text.toString().trim()) > Calendar.getInstance().get(Calendar.YEAR) - 5
                        && edtNumOfAccident.text.toString().toInt() == 0
                        && !carType.equals("Sport")){
                        INSURANCE_TYPE = "gold"
                        imgGold.setImageDrawable(resources.getDrawable(R.drawable.ic_tick))
                        imgDiamond.setImageDrawable(null)
                        imgSilver.setImageDrawable(null)
                        txtDetails.visibility = View.VISIBLE
                        txtDetails.text = "gold insurance"
                    }else{
                        INSURANCE_TYPE = "silver"
                        imgSilver.setImageDrawable(resources.getDrawable(R.drawable.ic_tick))
                        imgGold.setImageDrawable(null)
                        imgDiamond.setImageDrawable(null)
                        txtDetails.visibility = View.VISIBLE
                        txtDetails.text = "silver insurance"
                    }
                }
            }
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAddAttachs -> {
                TakePhotoUtils.getInstance()?.SelectPhotoDialog(activity!!, this)
            }
            R.id.relDiamond -> {
                selectInsuranceType(R.id.relDiamond)
            }
            R.id.relGolden -> {
                selectInsuranceType(R.id.relGolden)
            }
            R.id.relSilver -> {
                selectInsuranceType(R.id.relSilver)
            }
            R.id.btnSave -> {
                validate()
            }
            R.id.relFees -> {
                inflatePaymentDialog()
            }
        }
    }

    private fun selectInsuranceType(id: Int) {
        when (id) {
            R.id.relDiamond -> {
                INSURANCE_TYPE = "diamond"
                imgDiamond.setImageDrawable(resources.getDrawable(R.drawable.ic_tick))
                imgGold.setImageDrawable(null)
                imgSilver.setImageDrawable(null)
                txtDetails.visibility = View.VISIBLE
                txtDetails.text = "diamond insurance"
            }
            R.id.relGolden -> {
                INSURANCE_TYPE = "gold"
                imgGold.setImageDrawable(resources.getDrawable(R.drawable.ic_tick))
                imgDiamond.setImageDrawable(null)
                imgSilver.setImageDrawable(null)
                txtDetails.visibility = View.VISIBLE
                txtDetails.text = "gold insurance"
            }
            R.id.relSilver -> {
                INSURANCE_TYPE = "silver"
                imgSilver.setImageDrawable(resources.getDrawable(R.drawable.ic_tick))
                imgGold.setImageDrawable(null)
                imgDiamond.setImageDrawable(null)
                txtDetails.visibility = View.VISIBLE
                txtDetails.text = "silver insurance"
            }
        }
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

    private fun validate() {
        if (INSURANCE_TYPE == null) {
            Toast.makeText(context, "Select Insurance Type!", Toast.LENGTH_SHORT).show()
            return
        }
        if (attachItems?.size == 0) {
            Toast.makeText(context, "Add Car Images!", Toast.LENGTH_SHORT).show()
            return
        }
        if (edtCarName.text.toString().trim().isEmpty()) {
            edtCarName.error = "Required Field !"
            return
        }
        if (edtCarModel.text.toString().trim().isEmpty()) {
            edtCarModel.error = "Required Field !"
            return
        }

        if (INSURANCE_TYPE.equals("gold") || INSURANCE_TYPE.equals("diamond")) {
            if (Integer.valueOf(edtCarModel.text.toString().trim())
                < Calendar.getInstance().get(Calendar.YEAR) - 5
            ) {
                edtCarModel.error =
                    "Model should not be less than ${(Calendar.getInstance().get(Calendar.YEAR) - 5)}"
                return
            }
        }

        if (carType == null) {
            Toast.makeText(context, "Select Car Type!", Toast.LENGTH_SHORT).show()
            return
        }

        if (edtCarColor.text.toString().trim().isEmpty()) {
            edtCarColor.error = "Required Field !"
            return
        }

        if (edtFees.text.toString().trim().isEmpty()) {
            edtFees.error = "Required Field !"
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
            SharedPrefManager.getInstance(context = context)?.getToken(), Id, "insurance"
            , INSURANCE_TYPE, edtCarName.text.toString().trim(), edtCarModel.text.toString().trim()
            , edtCarColor.text.toString().trim(), carType,
            paymentItem?.cardNum,
            paymentItem?.eDate,
            paymentItem?.name,
            paymentItem?.money,
            paymentItem?.cvv,
            uploadFilesTemp.toTypedArray()
        )

    }

    override fun afterTextChanged(s: Editable?) {
        if (edtCarModel.text.toString().trim() != "")
            if (INSURANCE_TYPE.equals("gold") || INSURANCE_TYPE.equals("diamond")) {
                if (Integer.valueOf(edtCarModel.text.toString().trim())
                    < Calendar.getInstance().get(Calendar.YEAR) - 5
                ) {
                    edtCarModel.error =
                        "Model should not be less than ${(Calendar.getInstance().get(Calendar.YEAR) - 5)}"
                }
            }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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
        paymentItem = PaymentItem(cardNum, eDate, name, money, cvv)
        edtFees.setText("Your card information will be reviewed")
        paymentDialog?.dismiss()
    }

}

