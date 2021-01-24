package com.uxi.bambupaymerchant.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.utils.Constants
import com.uxi.bambupaymerchant.utils.isSunmiDevice
import com.uxi.bambupaymerchant.view.activity.ScanCodeActivity.Companion.CAMERA_REQUEST_CODE
import com.uxi.bambupaymerchant.view.activity.ScanCodeActivity.Companion.SCANNED_QR_CODE
import com.uxi.bambupaymerchant.view.activity.ScanCodeActivity.Companion.SCAN_QR_CODE
import com.uxi.bambupaymerchant.view.fragment.dialog.SuccessDialog
import com.uxi.bambupaymerchant.viewmodel.AcceptTransactionViewModel
import com.uxi.bambupaymerchant.viewmodel.UserTokenViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_cash_in.*
import kotlinx.android.synthetic.main.content_cash_in.btn_cancel
import kotlinx.android.synthetic.main.content_cash_in.btn_scan_qr_code
import kotlinx.android.synthetic.main.content_cash_in.btn_transact
import timber.log.Timber
import java.util.ArrayList
import java.util.HashMap


/**
 * Created by Eraño Payawal on 8/3/20.
 * hunterxer31@gmail.com
 */
class CashInActivity : BaseActivity() {

    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val cashInViewModel by viewModels<AcceptTransactionViewModel> { viewModelFactory }

    private val transactionType: String? by lazy {
        intent?.extras?.getString(Constants.MODE_OF_TRANSACTION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    override fun getLayoutId() = R.layout.activity_cash_in

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

    private fun titleBar(): String {
        return when {
            transactionType.equals(Constants.CASH_IN) -> {
                getString(R.string.menu_client_transfer)
            }
            transactionType.equals(Constants.CASH_OUT) -> {
                getString(R.string.menu_post_transfer)
            }
            transactionType.equals(Constants.SEND_MONEY) -> {
                getString(R.string.menu_batch_transfer)
            }
            else -> ""
        }
    }

    private fun titleHeader(): String {
        return when {
            transactionType.equals(Constants.CASH_IN) -> {
                getString(R.string.cash_in)
            }
            transactionType.equals(Constants.CASH_OUT) -> {
                getString(R.string.cash_out)
            }
            transactionType.equals(Constants.SEND_MONEY) -> {
                getString(R.string.send_money)
            }
            else -> ""
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        tv_toolbar_title?.text = titleBar()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun initView() {
        text_title_header.text = titleHeader()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isSunmiDevice()) {
                    showSunmiCamera()
                } else {
                    showScanCode()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SCAN_QR_CODE) {
            val qrCode: String? = data?.getStringExtra(SCANNED_QR_CODE)
            Log.e("DEBUG", "qrCode:: $qrCode")
            qrCode?.let {
                text_input_transact_num.setText(it)
            }
        } else if (requestCode == ScanPayQrCodeActivity.START_SCAN) {
            val bundle = data?.extras
            val result = bundle?.getSerializable("data") as ArrayList<HashMap<String, String>>?
            result?.let {
                if (it.size > 0) {
                    val dataItem = it[0].getValue("VALUE")
                    Timber.tag("DEBUG").e("dataItem:: $dataItem")
                    if (!dataItem.isNullOrEmpty()) {
                        text_input_transact_num.setText(dataItem)
                    }
                }
            }
        }
    }

    override fun initData() {

    }

    override fun events() {
        btn_scan_qr_code.setOnClickListener {
            cameraPermission()
        }
        btn_transact.setOnClickListener {
            transaction()
        }
        btn_cancel.setOnClickListener {
            onBackPressed()
        }
    }

    private fun cameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        } else {
            if (isSunmiDevice()) {
                showSunmiCamera()
            } else {
                showScanCode()
            }
        }
    }

    private fun showScanCode() {
        val intent = Intent(this@CashInActivity, ScanCodeActivity::class.java)
        startActivityForResult(intent, SCAN_QR_CODE)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }

    private fun showSunmiCamera() {
        val intent = Intent()
        intent.action = "com.sunmi.scan"
        intent.setPackage("com.sunmi.codescanner")
        intent.setClassName(
            "com.sunmi.sunmiqrcodescanner",
            "com.sunmi.sunmiqrcodescanner.activity.ScanActivity"
        )
        startActivityForResult(intent, ScanPayQrCodeActivity.START_SCAN)
    }

    override fun observeViewModel() {
        cashInViewModel.isTransactionNumberEmpty.observe(this, Observer { isTransactionNumberEmpty ->
            if (isTransactionNumberEmpty) {
                showDialogMessage("Transaction Number Required")
            }
        })

        /*cashInViewModel.successMessage.observe(this, Observer { successMessage ->
            if (!successMessage.isNullOrEmpty()) {
                alertMessage(successMessage)
            }
        })*/

        cashInViewModel.errorMessage.observe(this, Observer { failedMessage ->
            if (!failedMessage.isNullOrEmpty()) {
                showDialogMessage(failedMessage)
            }
        })

        cashInViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })

        cashInViewModel.isSuccess.observe(this, Observer { isSuccess ->
            if (!isSuccess) {
                // call token refresher
                userTokenModel.subscribeToken()
            }
        })

        userTokenModel.isTokenRefresh.observe(this, Observer { isTokenRefresh ->
            if (isTokenRefresh) {
                transaction()
            }
        })

        cashInViewModel.cashInDataWithMessage.observe(this, Observer { it1 ->
            it1?.let {
                if (!it.first.isNullOrEmpty() && it.second != null) {
                    val amount = it.second?.amount
                    val dialog = SuccessDialog(
                        ctx = this@CashInActivity,
                        message = it.first,
                        amount = amount,
                        date = it.second?.timestamp,
                        qrCodeUrl = null,
                        txFee = it.second?.fee,
                        onNewClicked = ::viewNewClick,
                        onDashBoardClicked = ::viewDashboardClick
                    )
                    dialog.show()
                }
            }
        })

    }

    private fun viewNewClick() {
        text_input_transact_num.setText("")
    }

    private fun viewDashboardClick() {
        showMain()
    }

    private fun alertMessage(message: String) {
        AlertDialog.Builder(this@CashInActivity, R.style.DialogStyle)
//            .setTitle("")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ -> onBackPressed() }
            .create()
            .show()
    }

    private fun transaction() {
        if (!transactionType.isNullOrEmpty()) {
            when {
                transactionType.equals(Constants.CASH_IN) -> {
                    cashInViewModel.subscribeCashIn(text_input_transact_num.text.toString())
                }
                transactionType.equals(Constants.CASH_OUT) -> {
                    cashInViewModel.subscribeCashOut(text_input_transact_num.text.toString())
                }
                transactionType.equals(Constants.SEND_MONEY) -> {
                    cashInViewModel.subscribeSendMoney(text_input_transact_num.text.toString())
                }
            }
        }
    }

}