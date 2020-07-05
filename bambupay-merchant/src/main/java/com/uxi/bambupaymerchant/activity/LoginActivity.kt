package com.uxi.bambupaymerchant.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.uxi.bambupaymerchant.R
import kotlinx.android.synthetic.main.activity_login.*

//import com.uxi.bambupay.viewmodel.LoginViewModel

class LoginActivity : BaseActivity(), View.OnClickListener {

//    private val viewModelLogin by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Timber.tag("DEBUG").e("isLoggedIn:: ${utils?.isLoggedIn}")
        observeViewModel()
        events()
    }

    override fun getLayoutId() = R.layout.activity_login

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_sign_in -> {
//                viewModelLogin.subscribeLogin(input_email.text.toString(), input_password.text.toString())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.btn_forgot_password -> {
                val intent = Intent(this, ForgotPasswordActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }
        }
    }

    private fun observeViewModel() {
//        viewModelLogin.subscribeToken()
//
//        viewModelLogin.loading.observe(this, Observer { isLoading ->
//            if (isLoading) {
//                showProgressDialog("Loading...")
//            } else {
//                dismissProgressDialog()
//            }
//        })
//
//        viewModelLogin.isSuccessLoggedIn.observe(this, Observer { isSuccessLoggedIn ->
//            if (isSuccessLoggedIn) {
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            } else {
//                viewModelLogin.subscribeToken()
//            }
//        })
//
//        viewModelLogin.refreshLogin.observe(this, Observer { isrefreshLogin ->
//            if (isrefreshLogin) {
//                Log.e("DEBUG", "isrefreshLogin")
//                viewModelLogin.subscribeLogin(input_email.text.toString(), input_password.text.toString())
//            }
//        })
    }

    private fun events() {
        btn_sign_in.setOnClickListener(this)
        btn_forgot_password.setOnClickListener(this)
    }
}