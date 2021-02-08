package com.uxi.bambupaymerchant.view.fragment.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.utils.SunmiPrintHelper
import com.uxi.bambupaymerchant.utils.isSunmiDevice
import kotlinx.android.synthetic.main.dialog_message_result.*

/**
 * Created by Eraño Payawal on 10/4/20.
 * hunterxer31@gmail.com
 */
class SuccessDialog(
    private val ctx: Context,
    private val message: String?,
    private val amount: String?,
    private val date: String?,
    private val qrCodeUrl: String?,
    private val txFee: String?,
    private val onNewClicked: () -> Unit,
    private val onDashBoardClicked: () -> Unit,
    private val isTransactionVisible: Boolean? = true) : Dialog(ctx) {

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_message_result)
        initViews()
    }

    private fun initViews() {
        message?.let {
            text_success_message.text = it
        }

        amount?.let {
            text_amount.text = ctx.getString(R.string.amount_value, it)
        }

        date?.let {
            text_date.text = it
        }

        if (!qrCodeUrl.isNullOrEmpty()) {
            image_view_qr_code.visibility = View.VISIBLE
            loadImage(qrCodeUrl, image_view_qr_code)
        }

        isTransactionVisible?.let {
            if (it) {
                btn_new_transaction.visibility = View.VISIBLE
            } else {
                btn_new_transaction.visibility = View.GONE
            }
        }

        if (txFee.isNullOrEmpty()) {
            text_fee_lbl.visibility = View.GONE
            text_fee.visibility = View.GONE
        }

        txFee?.let {
            text_fee.text = ctx.getString(R.string.amount_value, it)
        }

        btn_new_transaction.setOnClickListener {
            dismiss()
            onNewClicked()
        }

        btn_okay.setOnClickListener {
            dismiss()
            onDashBoardClicked()
        }

        btn_print.setOnClickListener {
            printDetails()
        }

        if (isSunmiDevice()) {
            btn_print.visibility = View.VISIBLE
        } else {
            btn_print.visibility = View.GONE
        }
    }

    private var qrCodeBitmap: Bitmap? = null
    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(ctx)
            .asBitmap()
            .load(imageUrl)
            .thumbnail(1.0f)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    qrCodeBitmap = resource
                    imageView.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }

    private fun printDetails() {
        SunmiPrintHelper.getInstance().printText("${text_success_message.text}\n\n", 25F, true)
        SunmiPrintHelper.getInstance().printText("${text_amount_lbl.text}\n", 20F, false)
        SunmiPrintHelper.getInstance().printText("${text_amount.text}\n\n", 40F, true)

        if (!txFee.isNullOrEmpty()) {
            SunmiPrintHelper.getInstance().printText("${text_fee_lbl.text}\n", 20F, true)
            SunmiPrintHelper.getInstance().printText("${text_fee.text}\n\n", 20F, true)
        }

        SunmiPrintHelper.getInstance().printText("${text_date.text}\n\n", 20F, true)
        if (image_view_qr_code.isVisible && qrCodeBitmap != null) {
//            SunmiPrintHelper.getInstance().printQr("www.sunmi.com", 6, 3)
            SunmiPrintHelper.getInstance().printBitmap(qrCodeBitmap, 1)
        }
        SunmiPrintHelper.getInstance().print2Line()
        SunmiPrintHelper.getInstance().feedPaper()
    }

}