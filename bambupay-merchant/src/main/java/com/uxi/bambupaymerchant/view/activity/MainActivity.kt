package com.uxi.bambupaymerchant.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.amulyakhare.textdrawable.TextDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.navigation.NavigationView
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.utils.Constants.Companion.CASH_IN
import com.uxi.bambupaymerchant.utils.Constants.Companion.CASH_OUT
import com.uxi.bambupaymerchant.utils.Constants.Companion.MODE_OF_TRANSACTION
import com.uxi.bambupaymerchant.utils.Constants.Companion.SEND_MONEY
import com.uxi.bambupaymerchant.view.activity.BaseActivity
import com.uxi.bambupaymerchant.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.app_bar_main.view.*


class MainActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private var initialDrawable: TextDrawable? = null
    private var avatarImageView: ImageView? = null
    private var qrCodeImageView: ImageView? = null
    private var nameTextView: TextView? = null
    private var mobileTextView: TextView? = null

    private val viewModelMain by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId() = R.layout.activity_main

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout?.isDrawerOpen(GravityCompat.START)) {
            drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupDrawerLayout() {
        drawerLayout = findViewById(R.id.drawer_layout)

//        val toggle = ActionBarDrawerToggle(
//            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
//        )
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()

        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home,
            R.id.nav_settings
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val headerView = navView.getHeaderView(0)
        avatarImageView = headerView.findViewById(R.id.image_avatar)
        qrCodeImageView = headerView.findViewById(R.id.image_qr_code)
        nameTextView = headerView.findViewById(R.id.text_full_name)
        mobileTextView = headerView.findViewById(R.id.text_mobile_num)
        if (initialDrawable != null) {
            avatarImageView?.setImageDrawable(initialDrawable)
        }

        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_client_fund_transfer -> {
                    val intent = Intent(this@MainActivity, CashInActivity::class.java)
                    intent.putExtra(MODE_OF_TRANSACTION, CASH_IN)
                    startActivity(intent)
                    overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
                    true
                }
                R.id.nav_pos_fund_transfer -> {
                    val intent = Intent(this@MainActivity, CashInActivity::class.java)
                    intent.putExtra(MODE_OF_TRANSACTION, CASH_OUT)
                    startActivity(intent)
                    overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
                    true
                }
                R.id.nav_batch_fund_transfer -> {
                    val intent = Intent(this@MainActivity, CashInActivity::class.java)
                    intent.putExtra(MODE_OF_TRANSACTION, SEND_MONEY)
                    startActivity(intent)
                    overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
                    true
                }
                R.id.nav_home -> {
                    navController.navigate(R.id.nav_home)
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_settings -> {
                    navController.navigate(R.id.nav_settings)
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_select_qr -> {
                    navController.navigate(R.id.nav_select_qr)
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
    }

    override fun initData() {

    }

    override fun initView() {
        setupToolbar()
        setupDrawerLayout()
    }

    override fun events() {

    }

    override fun observeViewModel() {
        viewModelMain.getCurrUser()

        viewModelMain.initials.observe(this, Observer { initials ->
            initials?.let {
                initialDrawable = TextDrawable.builder()
                    .beginConfig()
                    .width(80) // width in px
                    .height(80) // height in px
                    .fontSize(32)
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRound(initials, ContextCompat.getColor(this,
                        R.color.green
                    ))

//                profile_image?.setImageDrawable(initialDrawable)
                avatarImageView?.setImageDrawable(initialDrawable)
            }
        })

        viewModelMain.avatarUrl.observe(this, Observer {
            loadImage(it, avatarImageView!!)
        })


        viewModelMain.fullName.observe(this, Observer { fullName ->
            fullName?.let {
                nameTextView?.text = it
            }
        })

        viewModelMain.mobileNumber.observe(this, Observer { mobileNumber ->
            mobileNumber?.let {
                mobileTextView?.text = it
            }
        })

        viewModelMain.qrCode.observe(this, Observer { qrCode ->
            qrCode?.let {
                qrCodeImageView?.let { it1 -> loadImage(it, it1) }
            }
        })
    }

    fun updateToolbar(title: String?, color: Int) {
        toolbar?.text_title.text = title
        toolbar?.setBackgroundColor(ResourcesCompat.getColor(resources, color, null))
    }

    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(this@MainActivity)
            .load(imageUrl)
            .thumbnail(1.0f)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(initialDrawable)
            .into(imageView)
    }

}