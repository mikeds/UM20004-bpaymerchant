package com.uxi.bambupaymerchant.ui.home

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.model.Transaction
import com.uxi.bambupaymerchant.ui.home.adapter.RecentHistoryAdapter
import com.uxi.bambupaymerchant.view.activity.TransactionHistoryActivity
import com.uxi.bambupaymerchant.view.fragment.BaseFragment
import com.uxi.bambupaymerchant.viewmodel.UserTokenViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber

class HomeFragment : BaseFragment() {

    private val homeViewModel by viewModels<HomeViewModel> { viewModelFactory }
    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val historyViewModel by viewModels<HistoryViewModel> { viewModelFactory }

    private lateinit var adapterRecent: RecentHistoryAdapter

    override fun getLayoutId() = R.layout.fragment_home

    override fun initData() {
        userTokenModel.subscribeToken()
    }

    override fun initView() {
        adapterRecent = RecentHistoryAdapter(
            activity = requireActivity(),
            onHistoryItemClick = ::onHistoryItemClick
        )

        recycler_view_recent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterRecent
            val decorator = DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
            decorator.setDrawable(activity?.let { ContextCompat.getDrawable(it, R.drawable.divider) }!!)
            addItemDecoration(decorator)
        }

        refresh_layout.setOnRefreshListener {
            historyViewModel.subscribeHistory(true)
            homeViewModel.subscribeBalance()
        }

        historyViewModel.subscribeHistory(true)
        homeViewModel.subscribeBalance()

        btn_view_all.setOnClickListener {
            val intent = Intent(activity, TransactionHistoryActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }
    }

    override fun observeViewModel() {
        homeViewModel.balanceData.observe(viewLifecycleOwner, { balance ->
            text_total_balance.text = getString(R.string.ph_currency, balance)
        })

        historyViewModel.getRecentHistory()
        historyViewModel.recentHistory.observe(viewLifecycleOwner, adapterRecent::submitList)
        historyViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                //shimmer_view_container.startShimmer()
            } else {
                //shimmer_view_container.stopShimmer()
                refresh_layout.finishRefresh()
                refresh_layout.finishLoadmore()
            }
        }
        historyViewModel.refreshToken.observe(viewLifecycleOwner, {
            Timber.tag("DEBUG").e("refreshToken::$it")
            if (it) {
                userTokenModel.subscribeToken()
            }
        })

        userTokenModel.isTokenRefresh.observe(viewLifecycleOwner, {
            Timber.tag("DEBUG").e("isTokenRefresh::$it")
            historyViewModel.subscribeHistory(true)
            homeViewModel.subscribeBalance()
        })
    }

    private fun onHistoryItemClick(transaction: Transaction) {

    }
}