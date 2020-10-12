package com.uxi.bambupaymerchant.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.ui.home.HomeViewModel
import com.uxi.bambupaymerchant.viewmodel.UserTokenViewModel
import timber.log.Timber

class HomeFragment : BaseFragment() {

    //    private lateinit var homeViewModel: HomeViewModel
    private val userTokenModel by viewModel<UserTokenViewModel>()

    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
        })
        return root
    }*/

    override fun getLayoutId() = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userTokenModel.subscribeToken()
        Timber.tag("DEBUG").e("subscribeToken")
    }
}