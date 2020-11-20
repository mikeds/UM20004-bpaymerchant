package com.uxi.bambupaymerchant.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.model.Transaction
import com.uxi.bambupaymerchant.ui.home.adapter.RecentHistoryAdapter
import com.uxi.bambupaymerchant.utils.makeVisibleOrGone
import com.uxi.bambupaymerchant.view.activity.BaseActivity
import com.uxi.bambupaymerchant.viewmodel.UserTokenViewModel
import kotlinx.android.synthetic.main.activity_transaction_history.*
import kotlinx.android.synthetic.main.app_toolbar.*
import timber.log.Timber

class TransactionHistoryActivity : BaseActivity() {

    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val historyViewModel by viewModels<HistoryViewModel> { viewModelFactory }

    private lateinit var adapterRecent: RecentHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    override fun getLayoutId() = R.layout.activity_transaction_history

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
        tv_toolbar_title?.text = getString(R.string.transaction_history)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun initData() {
        userTokenModel.subscribeToken()
    }

    override fun initView() {
        adapterRecent = RecentHistoryAdapter(
            activity = this@TransactionHistoryActivity,
            onHistoryItemClick = ::onHistoryItemClick
        )

        recycler_view_history.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterRecent
            val decorator = DividerItemDecoration(this@TransactionHistoryActivity, LinearLayoutManager.VERTICAL)
            ContextCompat.getDrawable(context, R.drawable.divider)?.let { decorator.setDrawable(it) }
            addItemDecoration(decorator)
        }
    }

    override fun observeViewModel() {
        historyViewModel.getHistory()
        historyViewModel.recentHistory.observe(this, adapterRecent::submitList)
        historyViewModel.isLoading.observe(this) { isLoading ->
            recycler_view_history.makeVisibleOrGone(!isLoading)
            shimmer_view_container.makeVisibleOrGone(isLoading)
            if (isLoading) {
                shimmer_view_container.startShimmer()
            } else {
                shimmer_view_container.stopShimmer()
                refresh_layout.finishRefresh()
                refresh_layout.finishLoadmore()
            }
        }
        historyViewModel.refreshToken.observe(this, {
            Timber.tag("DEBUG").e("refreshToken::$it")
            if (it) {
                userTokenModel.subscribeToken()
            }
        })

        userTokenModel.isTokenRefresh.observe(this, {
            Timber.tag("DEBUG").e("isTokenRefresh::$it")
            historyViewModel.subscribeHistory(true)
        })
        historyViewModel.subscribeHistory(true)
    }

    override fun events() {
        refresh_layout.setOnRefreshListener {
            historyViewModel.subscribeHistory(true)
        }

        refresh_layout?.setOnLoadmoreListener {
            historyViewModel.subscribeHistory(false)
        }
    }

    private fun onHistoryItemClick(transaction: Transaction) {
        val intent = Intent(this@TransactionHistoryActivity, TransactionDetailsActivity::class.java)
        intent.putExtra("transactionId", transaction.id)
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }
}