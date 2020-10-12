package com.uxi.bambupaymerchant.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.view.activity.CreateQRActivity
import com.uxi.bambupaymerchant.view.activity.ScanPayQrCodeActivity
import kotlinx.android.synthetic.main.fragment_select_qr_code.*

/**
 * Created by Eraño Payawal on 10/12/20.
 * hunterxer31@gmail.com
 */
class SelectQrCodeFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_select_qr_code

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_scan_pay_qr.setOnClickListener {
            activity?.let {
                val intent = Intent(it, ScanPayQrCodeActivity::class.java)
                startActivity(intent)
                it.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }
        }

        btn_create_pay_qr.setOnClickListener {
            activity?.let {
                val intent = Intent(it, CreateQRActivity::class.java)
                startActivity(intent)
                it.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }
        }
    }

}