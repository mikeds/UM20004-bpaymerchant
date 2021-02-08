package com.uxi.bambupaymerchant.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.utils.Constants
import kotlinx.android.synthetic.main.activity_select_cash_in.*
import kotlinx.android.synthetic.main.app_toolbar.*

/**
 * Created by Eraño Payawal on 2/8/21.
 * hunterxer31@gmail.com
 */
class SelectCashInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_cash_in)
        setupToolbar()
        events()
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
        setSupportActionBar(toolbar)
        tv_toolbar_title?.text = getString(R.string.cash_in)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun events() {
        btn_card.setOnClickListener {
            showCashInScreen(Constants.CASH_IN_CARD_SCREEN)
        }
        btn_bancnet.setOnClickListener {
            showCashInScreen(Constants.CASH_IN_BANCNET_SCREEN)
        }

        btn_grab_pay.setOnClickListener {
            showCashInScreen(Constants.CASH_IN_GRAB_SCREEN)
        }

        btn_gcash.setOnClickListener {
            showCashInScreen(Constants.CASH_IN_GCASH_SCREEN)
        }

        btn_paymaya.setOnClickListener {
            showCashInScreen(Constants.CASH_IN_PAYMAYA_SCREEN)
        }
    }

    private fun showCashInScreen(cashInType: String) {
        val intent = Intent(this@SelectCashInActivity, CashInCardActivity::class.java)
        intent.putExtra(Constants.SCREEN_FROM, cashInType)
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }

}