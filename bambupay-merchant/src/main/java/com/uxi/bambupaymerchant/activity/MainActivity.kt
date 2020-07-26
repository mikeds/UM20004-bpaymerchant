package com.uxi.bambupaymerchant.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.amulyakhare.textdrawable.TextDrawable
import com.google.android.material.navigation.NavigationView
import com.uxi.bambupaymerchant.R
import kotlinx.android.synthetic.main.app_bar_main.view.*


class MainActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private var initialDrawable: TextDrawable? = null
    private var avatarImageView: ImageView? = null
    private var nameTextView: TextView? = null
    private var mobileTextView: TextView? = null

//    private val viewModelMain by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupToolbar()
        setupDrawerLayout()
        observeViewModel()
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
            R.id.nav_home
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val headerView = navView.getHeaderView(0)
        avatarImageView = headerView.findViewById(R.id.image_avatar)
        nameTextView = headerView.findViewById(R.id.text_full_name)
        mobileTextView = headerView.findViewById(R.id.text_mobile_num)
        if (initialDrawable != null) {
            avatarImageView?.setImageDrawable(initialDrawable)
        }
    }

    private fun observeViewModel() {
        /*viewModelMain.getCurrUser()

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
                        R.color.light_green
                    ))

                profile_image?.setImageDrawable(initialDrawable)
                avatarImageView?.setImageDrawable(initialDrawable)
            }
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
        })*/
    }

    fun updateToolbar(title: String?, color: Int) {
        toolbar?.text_title.text = title
        toolbar?.setBackgroundColor(ResourcesCompat.getColor(resources, color, null))
    }

}