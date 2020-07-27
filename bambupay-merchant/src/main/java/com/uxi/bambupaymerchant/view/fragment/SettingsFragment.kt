package com.uxi.bambupaymerchant.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.view.activity.LoginActivity
import com.uxi.bambupaymerchant.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment() {

    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun getLayoutId() = R.layout.fragment_settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_logout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(context as Context)
        builder.setMessage(resources.getString(R.string.dialog_logout))
        builder.setNegativeButton(getString(R.string.action_cancel).toUpperCase(), null)
        builder.setPositiveButton(R.string.action_logout) { _, _ ->
            settingsViewModel.deleteLocalData()
            logout()
        }
        builder.create().show()
    }

    private fun logout() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}