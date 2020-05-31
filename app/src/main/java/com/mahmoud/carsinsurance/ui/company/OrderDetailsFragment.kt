package com.mahmoud.carsinsurance.ui.company


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmoud.carsinsurance.Injection
import com.mahmoud.carsinsurance.R
import com.mahmoud.carsinsurance.Utils.AppUtils
import com.mahmoud.carsinsurance.Utils.SharedPrefManager
import com.mahmoud.carsinsurance.models.AttachItem
import com.mahmoud.carsinsurance.models.GeneralResponse.Status
import com.mahmoud.carsinsurance.models.requestsResponse.OrdersItem
import com.mahmoud.carsinsurance.ui.company.SendMessage.SendMessageDialog
import com.mahmoud.carsinsurance.ui.company.SendMessage.SendMessageOnClick
import com.mahmoud.carsinsurance.ui.user.company.adapter.ImagesAdapter
import com.mahmoud.carsinsurance.ui.user.home.ViewModel.OrderViewModel
import com.stfalcon.frescoimageviewer.ImageViewer
import kotlinx.android.synthetic.main.fragment_insurance_request.relDiamond
import kotlinx.android.synthetic.main.fragment_insurance_request.relSilver
import kotlinx.android.synthetic.main.fragment_order_details.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class OrderDetailsFragment : Fragment(), View.OnClickListener, ImagesAdapter.OnItemClicked,
    SendMessageOnClick {

    private var HANDLE_TYPE: String? = null

    private var viewModel: OrderViewModel? = null
    private var navController: NavController? = null
    private var createMessageDialog: SendMessageDialog? = null
    private var attachItems: ArrayList<AttachItem>? = null
    private var imagesAdapter: ImagesAdapter? = null
    private var order: OrdersItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        order = arguments?.getParcelable<OrdersItem>("order")
        viewModel =
            ViewModelProvider(this, Injection.provideOrderViewModelFactory(context = context!!))
                .get(OrderViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        btnApprove.setOnClickListener(this)
        btnRefuse.setOnClickListener(this)
        btnBack.setOnClickListener(this)

        fillView()

        viewModel?.observeHandleOrder()?.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it.getStatus()) {
                    Status.Loading -> {
                        //createMessageDialog!!.isLoading(true)
                        AppUtils.getInstance()?.showWaiting(context!!)
                    }
                    Status.Failure -> {
                        //createMessageDialog!!.isLoading(false)
                        AppUtils.getInstance()?.dismissDialog()
                        Toast.makeText(
                            context,
                            it.getError()?.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Status.Success -> {
                        AppUtils.getInstance()?.dismissDialog()
                        //createMessageDialog!!.isLoading(false)
                        //createMessageDialog!!.dismiss()
                        //if (HANDLE_TYPE.equals("refuse"))
                            Toast.makeText(
                                context,
                                "message has been sent successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        CompanyHomeFragment.isRefresh=true
                        navController?.navigateUp()
                    }
                }
            })
    }

    private fun fillView() {
        if (order?.status != 3)
            relBtns.visibility = View.GONE
        else
            relBtns.visibility = View.VISIBLE


        when (order?.insuranceType) {
            "diamond" -> {
                relDiamond.visibility = View.VISIBLE
                relSilver.visibility = View.GONE
                relGold.visibility = View.GONE
            }
            "gold" -> {
                relDiamond.visibility = View.GONE
                relSilver.visibility = View.GONE
                relGold.visibility = View.VISIBLE
            }
            "silver" -> {
                relDiamond.visibility = View.GONE
                relSilver.visibility = View.VISIBLE
                relGold.visibility = View.GONE
            }
        }
        ////add attaches
        recAttachments.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, false
        )
        recAttachments.setHasFixedSize(true)
        attachItems = ArrayList()
        for (i in order?.attaches?.indices!!) {
            attachItems?.add(AttachItem("image", order?.attaches!![i]))
        }
        imagesAdapter = ImagesAdapter(attachItems)
        imagesAdapter!!.setIsAdd(false)
        recAttachments.adapter = imagesAdapter
        imagesAdapter!!.setOnItemClickListener(this)

        //fill data
        if (order?.type.equals("renew")) {
            edtAddress.visibility = View.VISIBLE
            edtCarNamee.visibility = View.GONE
            edtCarModell.visibility = View.GONE
            edtCarColorr.visibility = View.GONE
            edtCarTypee.visibility = View.GONE

            txtCarName.visibility = View.GONE
            txtCarModel.visibility = View.GONE
            txtCarColor.visibility = View.GONE
            txtCarType.visibility = View.GONE

            relDiamond.visibility = View.GONE
            relSilver.visibility = View.GONE
            relGold.visibility = View.GONE
            edtAddress.setText(order?.address)
        } else {
            edtAddress.visibility = View.GONE
            txtAdd.visibility = View.GONE
            edtCarNamee.setText(order?.carName)
            edtCarModell.setText(order?.carYear.toString())
            edtCarColorr.setText(order?.carColor)
            edtCarTypee.setText(order?.carType)
        }

        edtFeess.setText(order?.carType)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnApprove -> {
                HANDLE_TYPE = "approve"
                handleOrder("Your request has been approved and the representative will send to you")
                //inflateSendMessageDialog()
                /*viewModel?.handleOrder(
                    SharedPrefManager.getInstance(context)?.getToken()
                    , order?.id, 1, null
                )*/
            }
            R.id.btnRefuse -> {
                HANDLE_TYPE = "refuse"
                handleOrder("Sorry, your request was rejected")

                //inflateSendMessageDialog()
            }
            R.id.btnBack -> {
                navController?.navigateUp()
            }
        }
    }

    override fun showItem(isAdd: Boolean, position: Int) {
        val images: MutableList<String> =
            ArrayList()
        images.add(order?.attaches?.get(position)!!)
        ImageViewer.Builder(context, images)
            .setFormatter { s: String? -> s }
            .setStartPosition(0)
            .hideStatusBar(false)
            .allowZooming(true)
            .allowSwipeToDismiss(true)
            .show()
    }

    override fun removeItem(position: Int) {
    }

    private fun inflateSendMessageDialog() {
        createMessageDialog = SendMessageDialog()
        createMessageDialog?.show(childFragmentManager, "")
        createMessageDialog?.isCancelable = true
        createMessageDialog?.setOnClickView(this)
    }

    override fun onClick(msg: String?) {
        viewModel?.handleOrder(
            SharedPrefManager.getInstance(context)?.getToken()
            , order?.id, if(HANDLE_TYPE.equals("refuse")) 0 else 1 , msg
        )
    }

    private fun handleOrder(msg : String){
        viewModel?.handleOrder(
            SharedPrefManager.getInstance(context)?.getToken()
            , order?.id, if(HANDLE_TYPE.equals("refuse")) 0 else 1 , msg
        )
    }

}
