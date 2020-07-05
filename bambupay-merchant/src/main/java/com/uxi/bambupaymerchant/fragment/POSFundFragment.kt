package com.uxi.bambupaymerchant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.activity.MainActivity
import com.uxi.bambupaymerchant.ui.slideshow.POSFundViewModel

class POSFundFragment : Fragment() {

    private lateinit var POSFundViewModel: POSFundViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        POSFundViewModel = ViewModelProviders.of(this).get(POSFundViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_pos_fund, container, false)
//        val textView: TextView = root.findViewById(R.id.text_slideshow)
//        POSFundViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        val activity = activity as? MainActivity
        activity?.updateToolbar(activity.getString(R.string.menu_post_transfer), android.R.color.white)
        setHasOptionsMenu(true)

        return root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val activity = activity as? MainActivity
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.updateToolbar(activity.getString(R.string.dashboard), R.color.light_grey)
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}