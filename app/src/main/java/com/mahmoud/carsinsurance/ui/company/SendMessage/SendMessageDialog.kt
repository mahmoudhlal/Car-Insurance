package com.mahmoud.carsinsurance.ui.company.SendMessage

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mahmoud.carsinsurance.R
import kotlinx.android.synthetic.main.dialog_send_message.*
import java.util.*

class SendMessageDialog : DialogFragment() {
    private var sendMessageOnClick: SendMessageOnClick? = null
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
        return inflater.inflate(R.layout.dialog_send_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendMsg.setOnClickListener(mOnSendClickListener)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    private val mOnSendClickListener =
        View.OnClickListener {
            if (TextUtils.isEmpty(edtMessage.getText().toString().trim())) {
                return@OnClickListener
            }
            sendMessageOnClick!!.onClick(edtMessage.text.toString().trim())
        }


    fun setOnClickView(sendMessageOnClick: SendMessageOnClick?) {
        this.sendMessageOnClick = sendMessageOnClick
    }


    fun isLoading(isLoading: Boolean) {
        relProgress.visibility = if(isLoading) View.VISIBLE else View.GONE
    }


    override fun onDismiss(dialog: DialogInterface) {
        dialog.cancel()
    }

}