package com.mahmoud.carsinsurance.Utils


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.mahmoud.carsinsurance.R
import java.io.File
import java.io.FileOutputStream
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

class AppUtils {

    private val context: Context? = null
    private var dialog: Dialog? = null
    companion object {
        private var Instance: AppUtils? = null
        fun getInstance(): AppUtils? {
            if (Instance == null) Instance = AppUtils()
            return Instance
        }
    }

    //handle request WRITE_EXTERNAL_STORAGE before use this method
//To See It In Galley
    fun SaveImageInExternalStorage(finalBitmap: Bitmap) {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/saved_images")
        if (!myDir.exists()) {
            myDir.mkdirs()
        }
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val fname = "Image-$n.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //using this line you will be able to see saved images in the gallery view.
/*context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                Uri.parse("file://" + Environment.getExternalStorageDirectory())));*/
    }

    fun snackBar(activity: Activity, str: String?, vararg gravity: Int) {
        val parentLayout = activity.window.decorView
            .findViewById<View>(R.id.content)
        //View parentLayout = new Fragment().getView().findViewById(android.R.id.content);
        val snack = Snackbar.make(parentLayout, str!!, Snackbar.LENGTH_LONG)
        val view = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        if (gravity.size > 0) params.gravity = gravity[0] else params.gravity = Gravity.BOTTOM
        view.layoutParams = params
        view.setBackgroundColor(Color.parseColor("#16b0e4"))
        snack.duration = 2000
        snack.show()
    }

    fun makeSpinner(
        arrayAdapter: ArrayAdapter<*>,
        spinner: Spinner, color: Int, hasColor: Boolean,
        onItemSelectedListener: OnItemSelectedListener?
    ) { //modelList = new ArrayList<>();
//ArrayAdapter arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, carBrandsList);
//to change spinner text color
        if (hasColor) {
            spinner.viewTreeObserver.addOnGlobalLayoutListener {
                (spinner.selectedView as TextView).setTextColor(color)
            }
        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        arrayAdapter.notifyDataSetChanged()
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = onItemSelectedListener
    }
    //    public void showdialog() {
//        hud.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel("برجاء الانتظار")
//                .setCancellable(true)
//                .setAnimationSpeed(2)
//                .setDimAmount(0.5f)
//                .show();
//    }
//
//    public void dismis_dialog() {
//        hud.dismiss();
//    }
    fun showWaiting(context: Context) {
        val progressDialog =  AlertDialog.Builder(context);
        progressDialog.setView(R.layout.view_general_waiting);
        progressDialog.setCancelable(false);
        dialog = progressDialog.create()
        (dialog as AlertDialog?)!!.show()
    }

    fun dismissDialog() {
        dialog!!.dismiss()
    }


    fun handleException(context: Context?, t: Throwable) {
        when (t) {
            is SocketTimeoutException -> makeToast(context, "خطأ فى الانترنت")
            is UnknownHostException -> makeToast(context, "خطأ فى الاتصال")
            is ConnectException -> makeToast(context, "خطأ فى الاتصال")
            else -> makeToast(context, t.localizedMessage)
        }
    }


    fun makeToast(context: Context?, msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun makeAlterDialog(
        context: Context?, title: String?, msg: String?,
        txtPositive: String?, onPositiveClickListener: DialogInterface.OnClickListener?,
        txtNegative: String?, onNegativeClickListener: DialogInterface.OnClickListener?
    ) {
        val builder =
            AlertDialog.Builder(context!!)
        builder.setTitle(title).setIcon(R.mipmap.ic_launcher)
        builder.setMessage(msg)
            .setCancelable(false)
            .setPositiveButton(txtPositive, onPositiveClickListener)
            .setNegativeButton(txtNegative, onNegativeClickListener)
        val alert = builder.create()
        alert.show()
    }


    fun makeSuccessAlterDialog(
        context: Context?, title: String?, msg: String?,
        txtPositive: String?, onPositiveClickListener: DialogInterface.OnClickListener?
    ) {
        val builder =
            AlertDialog.Builder(context!!)
        builder.setTitle(title).setIcon(R.mipmap.ic_launcher)
        builder.setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("OK",onPositiveClickListener)
        val alert = builder.create()
        alert.show()
    }


}