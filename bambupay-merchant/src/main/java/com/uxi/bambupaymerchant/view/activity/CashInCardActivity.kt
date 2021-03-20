package com.uxi.bambupaymerchant.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.egpayawal.card_library.view.CreditCardExpiryTextWatcher
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.databinding.ActivityCashInCardBinding
import com.uxi.bambupaymerchant.utils.Constants
import com.uxi.bambupaymerchant.utils.Constants.Companion.CASH_IN_REDIRECT_URL
import com.uxi.bambupaymerchant.view.ext.viewBinding
import com.uxi.bambupaymerchant.viewmodel.CashInViewModel

/**
 * Created by Eraño Payawal on 12/15/20.
 * hunterxer31@gmail.com
 */
class CashInCardActivity : BaseActivity() {

    private val cashInViewModel by viewModels<CashInViewModel> { viewModelFactory }
    private val binding by viewBinding(ActivityCashInCardBinding::inflate)

    private val fromScreen by lazy {
        intent?.getStringExtra(Constants.SCREEN_FROM)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
        initViews()
        observeViewModel()
        events()
    }

    override fun getLayoutId() = R.layout.activity_cash_in_card

    override fun initData() {

    }

    override fun initView() {
        fromScreen?.let {
            if (it == Constants.CASH_IN_BANCNET_SCREEN) {
                binding.content.textMinimumLbl.visibility = View.GONE
            }
        }
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

    private fun setupToolbar() {
        setSupportActionBar(binding.appToolbar.toolbar)
        val title = when (fromScreen) {
            Constants.CASH_IN_CARD_SCREEN -> {
                getString(R.string.cash_in_card)
            }
            Constants.CASH_IN_BANCNET_SCREEN -> {
                getString(R.string.cash_in_bancnet)
            }
            Constants.CASH_IN_GRAB_SCREEN -> {
                getString(R.string.cash_in_grab_pay)
            }
            Constants.CASH_IN_GCASH_SCREEN -> {
                getString(R.string.cash_in_gcash)
            }
            Constants.CASH_IN_PAYMAYA_SCREEN -> {
                getString(R.string.cash_in_paymaya)
            }
            else -> getString(R.string.cash_in_card)
        }

        binding.appToolbar.tvToolbarTitle.text = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun initViews() {
        binding.content.textInputCardExpiration.addTextChangedListener(CreditCardExpiryTextWatcher(binding.content.textInputCardExpiration))
    }

    override fun observeViewModel() {
        cashInViewModel.isLoading.observe(this, Observer {
            if (it) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })

        cashInViewModel.paynamicsData.observe(this, Observer { paynamics ->
            paynamics?.redirectUrl?.let {
                showPaynamicsWebview(it)
            }
        })

        cashInViewModel.errorMessage.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                showMessageDialog(it)
            }
        })
    }

    private fun viewNewClick() {
        binding.content.textInputAmount.setText("")
        binding.content.textInputCardNumber.setText("")
        binding.content.textInputCardExpiration.setText("")
        binding.content.textInputCvv.setText("")
        binding.content.textInputCardName.setText("")
    }

    private fun viewDashboardClick() {
        showMain()
    }

    override fun events() {
        binding.content.btnTransact.setOnClickListener {
            cashInViewModel.subscribeCashInPaynamics(binding.content.textInputAmount.text.toString(), fromScreen)
        }

        binding.content.btnCancel.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showPaynamicsWebview(url: String) {
        val intent = Intent(this@CashInCardActivity, CashInPaynamicsActivity::class.java)
        intent.putExtra(Constants.AMOUNT, binding.content.textInputAmount.text.toString())
        intent.putExtra(CASH_IN_REDIRECT_URL, url)
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }

}