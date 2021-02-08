package com.uxi.bambupaymerchant.view.activity

import android.graphics.Bitmap
import android.net.UrlQuerySanitizer
import android.os.Bundle
import android.view.MenuItem
import android.webkit.*
import com.uxi.bambupaymerchant.BuildConfig
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.utils.Constants
import com.uxi.bambupaymerchant.view.fragment.dialog.SuccessDialog
import kotlinx.android.synthetic.main.activity_paynamics.*
import kotlinx.android.synthetic.main.app_toolbar.*
import timber.log.Timber
import java.net.URI

/**
 * Created by Eraño Payawal on 1/30/21.
 * hunterxer31@gmail.com
 */
class CashInPaynamicsActivity : BaseActivity() {

    private val redirectUrl by lazy {
        intent?.getStringExtra(Constants.CASH_IN_REDIRECT_URL)
    }

    private val amount by lazy {
        intent?.getStringExtra(Constants.AMOUNT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    override fun getLayoutId() = R.layout.activity_paynamics

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        tv_toolbar_title?.text = getString(R.string.cash_in)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun initData() {

    }

    override fun initView() {
        web_view.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (!loadingFinished) {
                    redirect = true
                }
                loadingFinished = false
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                loadingFinished = false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (!redirect) {
                    loadingFinished = true

                    Timber.tag("DEBUG").e("onPageFinished $url")

                    parseUrl(url)


                } else {
                    redirect = false
                }
            }
        }

        val webSettings: WebSettings = web_view.settings
        webSettings.javaScriptEnabled = true
        web_view.addJavascriptInterface(this, "androidObj")
        redirectUrl?.let {
            web_view.loadUrl(it)
        }
    }

    override fun observeViewModel() {

    }

    override fun events() {

    }

    private var loadingFinished = true
    private var redirect = false

    @JavascriptInterface
    fun showHTML(obj: String?) {
        Timber.tag("DEBUG").e("response data:: $obj")
    }

    private fun parseUrl(url: String?) {
        url?.let {

            if (it.contains(BuildConfig.API_BASE_URL)) {
                Timber.tag("DEBUG").e("passed response")

                val uri = URI(it)
                val scheme = uri.scheme
                val host = uri.host // HOST

                Timber.tag("DEBUG").e("Host:: $host")
                Timber.tag("DEBUG").e("API_BASE_URL:: ${BuildConfig.API_BASE_URL}")

                if (!host.isNullOrEmpty()) {
                    val query = uri.rawQuery
                    val urlQuerySanitizer = UrlQuerySanitizer(url)
                    val success = urlQuerySanitizer.getValue("success")
                    var message = urlQuerySanitizer.getValue("message")
                    message = message.replace("_", " ")
                    var timestamp = urlQuerySanitizer.getValue("timestamp")
                    timestamp = timestamp.replace("_", " ")
                    Timber.tag("DEBUG").e("Scheme:: $scheme")
                    Timber.tag("DEBUG").e("Query:: $query")
                    Timber.tag("DEBUG").e("message:: $message")
                    Timber.tag("DEBUG").e("timestamp:: $timestamp")

                    if (success == "true") {
                        val dialog = SuccessDialog(
                            ctx = this@CashInPaynamicsActivity,
                            message = message,
                            amount = amount,
                            date = timestamp,
                            qrCodeUrl = null,
                            txFee = "",
                            onNewClicked = ::viewNewClick,
                            onDashBoardClicked = ::viewDashboardClick,
                            isTransactionVisible = false
                        )
                        dialog.show()


                        dialog.show()
                    } else if (success == "false") {
                        showMessageDialog(message)
                    }
                }
            }
        }
    }

    private fun viewDashboardClick() {
        showMain()
    }

    private fun viewNewClick() {

    }

}