package com.uxi.bambupaymerchant.view.activity

import android.content.Intent
import android.os.Bundle
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.model.Splash
import com.uxi.bambupaymerchant.utils.Utils
import com.uxi.bambupaymerchant.view.adapter.SplashPagerAdapter
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
class SplashActivity : BaseActivity() {

    @Inject
    lateinit var utils: Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId() = R.layout.activity_splash

    override fun initData() {
        utils?.let {
            if (it.isLoggedIn) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
                finish()
            }
        }
    }

    override fun initView() {
        val images: ArrayList<Splash> = arrayListOf(
            Splash(R.drawable.img_page1, getString(R.string.page1), getString(R.string.page_demo)),
            Splash(R.drawable.img_page2, getString(R.string.page2), getString(R.string.page_demo)),
            Splash(R.drawable.img_page3, getString(R.string.page3), getString(R.string.page_demo))
        )

        val adapter = SplashPagerAdapter(
            this@SplashActivity,
            images
        )
        pager.adapter = adapter
        indicator.setViewPager(pager)
        indicator.isSnap = true

    }

    override fun observeViewModel() {

    }

    override fun events() {
        btn_skip.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            finish()
        }
    }
}