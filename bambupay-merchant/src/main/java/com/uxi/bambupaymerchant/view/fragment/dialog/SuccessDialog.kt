package com.uxi.bambupaymerchant.view.fragment.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.uxi.bambupaymerchant.R
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
    private val qrCodeUrl: String?) : Dialog(ctx) {

    private lateinit var clickListener: OnSuccessDialogClickListener

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
            if (it.isNotEmpty()) {
                text_amount_lbl.visibility = View.VISIBLE
                text_amount.visibility = View.VISIBLE
                text_amount.text = ctx.getString(R.string.amount_value, it)
            }
        }

        date?.let {
            text_date.text = it
        }

        if (!qrCodeUrl.isNullOrEmpty()) {
            image_view_qr_code.visibility = View.VISIBLE
            loadImage(qrCodeUrl, image_view_qr_code)
        }

        btn_new_transaction.setOnClickListener {
            if (this::clickListener.isInitialized) {
                dismiss()
                clickListener.onNewClicked()
            }
        }

        btn_okay.setOnClickListener {
            if (this::clickListener.isInitialized) {
                dismiss()
                clickListener.onDashBoardClicked()
            }
        }
    }

    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(ctx)
            .load(imageUrl)
            .thumbnail(1.0f)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }

    interface OnSuccessDialogClickListener {
        fun onDashBoardClicked()
        fun onNewClicked()
    }

    fun setOnSuccessDialogClickListener(listener: OnSuccessDialogClickListener) {
        clickListener = listener
    }

}