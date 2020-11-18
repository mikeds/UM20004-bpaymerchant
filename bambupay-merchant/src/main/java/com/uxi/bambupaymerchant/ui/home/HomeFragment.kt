package com.uxi.bambupaymerchant.ui.home

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.model.Transaction
import com.uxi.bambupaymerchant.ui.home.adapter.RecentHistoryAdapter
import com.uxi.bambupaymerchant.view.fragment.BaseFragment
import com.uxi.bambupaymerchant.viewmodel.UserTokenViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber

class HomeFragment : BaseFragment() {

    //    private lateinit var homeViewModel: HomeViewModel
    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val historyViewModel by viewModels<HistoryViewModel> { viewModelFactory }

    private lateinit var adapterRecent: RecentHistoryAdapter

    override fun getLayoutId() = R.layout.fragment_home

    override fun initData() {
        userTokenModel.subscribeToken()
        Timber.tag("DEBUG").e("subscribeToken")
    }

    override fun initView() {
        adapterRecent = RecentHistoryAdapter(
            activity = requireActivity(),
            onHistoryItemClick = ::onHistoryItemClick
        )

        recycler_view_recent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterRecent
        }

        refresh_layout.setOnRefreshListener {
            historyViewModel.subscribeHistory(true)
        }

        historyViewModel.subscribeHistory(true)
    }

    override fun observeViewModel() {
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
        })
    }

    private fun onHistoryItemClick(transaction: Transaction) {

    }
}