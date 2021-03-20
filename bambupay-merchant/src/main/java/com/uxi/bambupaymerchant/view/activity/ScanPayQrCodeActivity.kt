package com.uxi.bambupaymerchant.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.databinding.ActivityScanPayQrCodeBinding
import com.uxi.bambupaymerchant.utils.isSunmiDevice
import com.uxi.bambupaymerchant.view.ext.viewBinding
import com.uxi.bambupaymerchant.view.fragment.dialog.SuccessDialog
import com.uxi.bambupaymerchant.viewmodel.QRCodeViewModel
import com.uxi.bambupaymerchant.viewmodel.UserTokenViewModel
import timber.log.Timber
import java.util.*

/**
 * Created by Eraño Payawal on 10/12/20.
 * hunterxer31@gmail.com
 */
class ScanPayQrCodeActivity : BaseActivity() {

    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val qrCodeViewModel by viewModel<QRCodeViewModel>()

    private val binding by viewBinding(ActivityScanPayQrCodeBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
    }

    override fun getLayoutId() = R.layout.activity_scan_pay_qr_code

    override fun initData() {

    }

    override fun initView() {

    }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanCodeActivity.SCAN_QR_CODE) {
            val qrCode: String? = data?.getStringExtra(ScanCodeActivity.SCANNED_QR_CODE)
            Log.e("DEBUG", "qrCode:: $qrCode")
            qrCode?.let {
                binding.contentScanPay.textInputRefNum.setText(it)
                qrCodeViewModel.subscribeTxDetails(it)
            }
        } else if (requestCode == START_SCAN) {
            val bundle = data?.extras
            val result = bundle?.getSerializable("data") as ArrayList<HashMap<String, String>>?
            result?.let {
                if (it.size > 0) {
                    val dataItem = it[0].getValue("VALUE")
                    Timber.tag("DEBUG").e("dataItem:: $dataItem")
                    if (!dataItem.isNullOrEmpty()) {
                        binding.contentScanPay.textInputRefNum.setText(dataItem)
                    }
                }
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.appToolbar.toolbar)
        binding.appToolbar.tvToolbarTitle.text = getString(R.string.scan_pay)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun events() {
        binding.contentScanPay.btnScanQrCode.setOnClickListener {
            cameraPermission()
        }
        binding.contentScanPay.btnCancel.setOnClickListener {
            onBackPressed()
        }
        binding.contentScanPay.btnTransact.setOnClickListener {
            qrCodeViewModel.subscribeScanPayQr(binding.contentScanPay.textInputRefNum.text.toString())
        }
    }

    override fun observeViewModel() {
        qrCodeViewModel.isTransactionNumberEmpty.observe(this, Observer {
            if (it) {
                showDialogMessage("Transaction Number Required")
            }
        })

//        qrCodeViewModel.successMessage.observe(this, Observer { successMessage ->
//            if (!successMessage.isNullOrEmpty()) {
////                alertMessage(successMessage)
//            }
//        })

        qrCodeViewModel.errorMessage.observe(this, Observer { failedMessage ->
            if (!failedMessage.isNullOrEmpty()) {
                showDialogMessage(failedMessage)
            }
        })

        qrCodeViewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
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
                qrCodeViewModel.subscribeScanPayQr(binding.contentScanPay.textInputRefNum.text.toString())
            }
        })

        var message = ""
        qrCodeViewModel.quickPaySuccessMsg.observe(this, Observer { successMessage ->
            message = successMessage
        })
        qrCodeViewModel.quickPayQrWithMessage.observe(this, Observer { it1 ->
            it1?.let {
                if (!it.first.isNullOrEmpty() && it.second != null) {
                    val dialog = SuccessDialog(
                        ctx = this@ScanPayQrCodeActivity,
                        message = it.first,
                        amount = it.second?.amount,
                        date = it.second?.timestamp,
                        qrCodeUrl = it.second?.qrCode,
                        txFee = it.second?.fee,
                        onNewClicked = ::viewNewClick,
                        onDashBoardClicked = ::viewDashboardClick
                    )
                    dialog.show()
                }
            }
        })

        qrCodeViewModel.txDetails.observe(this, { it1 ->
            it1?.let {
                binding.contentScanPay.containerFee.visibility = View.VISIBLE
                binding.contentScanPay.textFee.text = it.fee
                binding.contentScanPay.textAmount.text = it.amount
            }
        })
    }

    private fun viewNewClick() {
        binding.contentScanPay.textInputRefNum.setText("")
        binding.contentScanPay.containerFee.visibility = View.GONE
        binding.contentScanPay.textFee.text = ""
        binding.contentScanPay.textAmount.text = ""
    }

    private fun viewDashboardClick() {
        showMain()
    }

    private fun cameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA), ScanCodeActivity.CAMERA_REQUEST_CODE
            )
        } else {
            if (isSunmiDevice()) {
                showSunmiCamera()
            } else {
                showScanCode()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ScanCodeActivity.CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isSunmiDevice()) {
                    showSunmiCamera()
                } else {
                    showScanCode()
                }
            }
        }
    }

    private fun showSunmiCamera() {
        val intent = Intent()
        intent.action = "com.sunmi.scan"
        intent.setPackage("com.sunmi.codescanner")
        //        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(
            "com.sunmi.sunmiqrcodescanner",
            "com.sunmi.sunmiqrcodescanner.activity.ScanActivity"
        )
        startActivityForResult(intent, START_SCAN)
    }

    private fun showScanCode() {
        val intent = Intent(this@ScanPayQrCodeActivity, ScanCodeActivity::class.java)
        startActivityForResult(intent, ScanCodeActivity.SCAN_QR_CODE)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }

    companion object {
        const val START_SCAN = 0x0001
    }
}