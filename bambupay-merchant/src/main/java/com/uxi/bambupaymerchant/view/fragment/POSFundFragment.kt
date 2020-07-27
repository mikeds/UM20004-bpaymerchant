package com.uxi.bambupaymerchant.view.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.view.activity.MainActivity
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
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