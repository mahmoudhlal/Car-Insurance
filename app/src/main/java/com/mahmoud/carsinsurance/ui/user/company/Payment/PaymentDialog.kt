package com.mahmoud.carsinsurance.ui.user.company.Payment

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.mahmoud.carsinsurance.R
import kotlinx.android.synthetic.main.dialog_payment.*
import kotlinx.android.synthetic.main.dialog_send_message.*
import kotlinx.android.synthetic.main.dialog_send_message.relProgress
import kotlinx.android.synthetic.main.dialog_send_message.sendMsg
import java.util.*

class PaymentDialog : DialogFragment() , View.OnClickListener {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    private var paymentOnClick: PaymentOnClick? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.NewDialog1)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setCanceledOnTouchOutside(true)
        return inflater.inflate(R.layout.dialog_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendMsg.setOnClickListener(mOnConfirmClickListener)
        relDate.setOnClickListener(this)
    }

    private val mOnConfirmClickListener =
        View.OnClickListener {
            if (TextUtils.isEmpty(edtCardNum.text.toString().trim())) {
                edtCardNum.error = "Required Field"
                return@OnClickListener
            }
            if (TextUtils.isEmpty(edtExpiration.text.toString().trim())) {
                edtExpiration.error = "Required Field"
                return@OnClickListener
            }
            if (TextUtils.isEmpty(edtCardHolder.text.toString().trim())) {
                edtCardHolder.error = "Required Field"
                return@OnClickListener
            }
            if (TextUtils.isEmpty(edtMoney.text.toString().trim())) {
                edtMoney.error = "Required Field"
                return@OnClickListener
            }
            if (TextUtils.isEmpty(edtCVV.text.toString().trim())) {
                edtCVV.error = "Required Field"
                return@OnClickListener
            }
            paymentOnClick!!.onClick(
                edtCardNum.text.toString().trim(),
                edtExpiration.text.toString().trim(),
                edtCardHolder.text.toString().trim(),
                edtMoney.text.toString().trim(),
                edtCVV.text.toString().trim())
        }


    fun setOnClickView(paymentOnClick: PaymentOnClick?) {
        this.paymentOnClick = paymentOnClick
    }


    fun isLoading(isLoading: Boolean) {
        relProgress.visibility = if(isLoading) View.VISIBLE else View.GONE
    }


    override fun onDismiss(dialog: DialogInterface) {
        dialog.cancel()
    }


    private fun datePicker() {
        val mCurrentTime = Calendar.getInstance()
        val yearNum = mCurrentTime[Calendar.YEAR]
        val monthNum = mCurrentTime[Calendar.MONTH]
        val dayNum = mCurrentTime[Calendar.DAY_OF_MONTH]
        DatePickerDialog(
            context!!,
            OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                edtExpiration.setText(
                    String.format(Locale.US, "%d / %d",  month + 1 , year/*, dayOfMonth*/))
            },
            yearNum,
            monthNum,
            dayNum
        ).show()
    }

    override fun onClick(v: View?) {
        datePicker()
    }


}