package com.uxi.bambupaymerchant.view.fragment

import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.model.Transaction
import com.uxi.bambupaymerchant.viewmodel.UserTokenViewModel

class HomeFragment : BaseFragment() {

    private val userTokenModel by viewModel<UserTokenViewModel>()
//    private val historyViewModel by viewModels<HistoryViewModel> { viewModelFactory }
//    private val historyViewModel by viewModel<HistoryViewModel>()

//    private lateinit var adapterRecent: RecentHistoryAdapter

    override fun getLayoutId() = R.layout.fragment_home

    override fun initData() {
        userTokenModel.subscribeToken()
    }

    override fun observeViewModel() {
//        historyViewModel.getRecentHistory()
        /*historyViewModel.recentHistory.observe(viewLifecycleOwner, adapterRecent::submitList)
        historyViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                //shimmer_view_container.startShimmer()
            } else {
                //shimmer_view_container.stopShimmer()
                refresh_layout.finishRefresh()
                refresh_layout.finishLoadmore()
            }
        }*/
//        historyViewModel.subscribeHistory(true)
    }

    override fun initView() {
        /*adapterRecent = RecentHistoryAdapter(
            activity = requireActivity(),
            onHistoryItemClick = ::onHistoryItemClick
        )*/

        /*recycler_view_recent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterRecent
        }*/

//        refresh_layout.setOnRefreshListener {
////            historyViewModel.subscribeHistory(true)
//        }
    }

    private fun onHistoryItemClick(transaction: Transaction) {

    }
}