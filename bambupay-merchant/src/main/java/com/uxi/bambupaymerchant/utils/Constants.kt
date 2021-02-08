package com.uxi.bambupaymerchant.utils

class Constants {
    companion object {
        const val SEND_MONEY = "Transfer"
        const val CASH_IN = "Cash-in (OTC)"
        const val CASH_OUT = "cash_out"
        const val SCAN_PAY_QR = "ScanPayQR"
        const val CREATE_SCAN_QR = "CreateScanQR"
        const val QUICK_PAY_QR = "QuickPayQR"
        const val INCOME_SHARES = "Income Shares"

        const val PENDING = "pending"
        const val APPROVED = "approved"
        const val CANCELLED = "cancelled"

        const val DEBIT = "debit"
        const val CREDIT = "credit"

        const val TYPE_OTC = "otc"

        const val AMOUNT = "amount"
        const val SCREEN_FROM = "screen_from"
        const val QUICK_SCAN_SCREEN = "screen_from"

        // TRANSACTION TYPE ID
        const val TX_TYPE_CASH_IN_OTC_ID = "txtype_cashin1"
        const val TX_TYPE_CASH_IN_DP_ID = "txtype_cashin2"
        const val TX_TYPE_CASH_OUT_OTC_ID = "txtype_cashout1"
        const val TX_TYPE_CREATE_SCAN_QR_ID = "txtype_createpayqr1"
        const val TX_TYPE_EN_CASH_ID = "txtype_encash1"
        const val TX_TYPE_INCOME_SHARES_ID = "txtype_income_shares"
        const val TX_TYPE_QUICK_PAY_QR_ID = "txtype_quickpayqr1"
        const val TX_TYPE_SCAN_PAY_QR_ID = "txtype_scanpayqr1"
        const val TX_TYPE_TOP_UP_OTC_ID = "txtype_topup1"
        const val TX_TYPE_TRANSFER_ID = "txtype_transfer1"
        const val TX_TYPE_VAULT_ID = "txtype_vault1"

        const val PASSWORD_MIN_LENGTH = 8

        const val MODE_OF_TRANSACTION = "mode_of_transaction"

        const val SUNMI_P2_EU = "SUNMI P2-EU"

        const val CASH_IN_CARD_SCREEN = "cash_in_card_screen"
        const val CASH_IN_BANCNET_SCREEN = "cash_in_bancnet_screen"
        const val CASH_IN_GRAB_SCREEN = "cash_in_grab_screen"
        const val CASH_IN_GCASH_SCREEN = "cash_in_gcash_screen"
        const val CASH_IN_PAYMAYA_SCREEN = "cash_in_paymaya_screen"

        const val CASH_IN_PAYNAMICS_CC = "cc"
        const val CASH_IN_PAYNAMICS_BANCNET = "bancnet"
        const val CASH_IN_PAYNAMICS_GRAB_PAY = "grabpay"
        const val CASH_IN_PAYNAMICS_GCASH = "gcash"
        const val CASH_IN_PAYNAMICS_PAYMAYA = "paymaya"
        const val CASH_IN_REDIRECT_URL = "redirect_url"
    }
}