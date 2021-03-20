package com.uxi.bambupaymerchant.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.databinding.ActivityCreateQrcodeBinding
import com.uxi.bambupaymerchant.view.ext.viewBinding
import com.uxi.bambupaymerchant.viewmodel.QRCodeViewModel
import com.uxi.bambupaymerchant.viewmodel.UserTokenViewModel

/**
 * Created by Eraño Payawal on 10/4/20.
 * hunterxer31@gmail.com
 */
class CreateQRActivity : BaseActivity() {

    private val qrCodeViewModel by viewModel<QRCodeViewModel>()
    private val userTokenModel by viewModel<UserTokenViewModel>()

    private val binding by viewBinding(ActivityCreateQrcodeBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
    }

    override fun initData() {

    }

    override fun initView() {

    }

    override fun getLayoutId() = R.layout.activity_create_qrcode

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.appToolbar.toolbar)
        binding.appToolbar.tvToolbarTitle.text = getString(R.string.create_qr)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun events() {
        binding.btnRetry.setOnClickListener {
            binding.textInputAmount.setText("")
            binding.imageViewQrCode.setImageDrawable(null)
            binding.containerButtons.visibility = View.GONE
            binding.btnGenerate.visibility = View.VISIBLE
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }

        binding.btnGenerate.setOnClickListener {
            it.hideKeyboard()
            binding.btnGenerate.visibility = View.GONE
            binding.containerButtons.visibility = View.VISIBLE
            qrCodeViewModel.subscribeCreatePayQr(binding.textInputAmount.text.toString())
        }
    }

    override fun observeViewModel() {
        qrCodeViewModel.isAmountEmpty.observe(this, Observer {
            if (it) {
                showDialogMessage("Amount Required")
            }
        })

        /*qrCodeViewModel.createPayQrData.observe(this, Observer { scanQR ->
            scanQR?.let {
                loadImage(it.qrCode!!, image_view_qr_code)
            }
        })*/

        qrCodeViewModel.createPayQrWithMessage.observe(this, Observer { it1 ->
            it1?.let {
                if (!it.first.isNullOrEmpty() && it.second != null) {
                    binding.txtQrSuccessMsg.visibility = View.VISIBLE
                    loadImage(it.second?.qrCode!!, binding.imageViewQrCode)
                }
            }
        })

        qrCodeViewModel.loading.observe(this, Observer {
            if (it) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })

        qrCodeViewModel.isSuccess.observe(this, Observer { isSuccess ->
            if (!isSuccess) {
                // call token refresher
                userTokenModel.subscribeToken()
            }
        })

        userTokenModel.isTokenRefresh.observe(this, Observer { isTokenRefresh ->
            if (isTokenRefresh) {
                qrCodeViewModel.subscribeCreatePayQr(binding.textInputAmount.text.toString())
            }
        })

    }

    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(this@CreateQRActivity)
            .load(imageUrl)
            .thumbnail(1.0f)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }

}