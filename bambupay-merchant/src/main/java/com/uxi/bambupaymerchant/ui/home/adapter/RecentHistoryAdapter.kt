package com.uxi.bambupaymerchant.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uxi.bambupaymerchant.R
import com.uxi.bambupaymerchant.model.Transaction
import com.uxi.bambupaymerchant.utils.Constants.Companion.CASH_IN
import com.uxi.bambupaymerchant.utils.Constants.Companion.CASH_OUT
import com.uxi.bambupaymerchant.utils.Constants.Companion.CREATE_SCAN_QR
import com.uxi.bambupaymerchant.utils.Constants.Companion.DEBIT
import com.uxi.bambupaymerchant.utils.Constants.Companion.INCOME_SHARES
import com.uxi.bambupaymerchant.utils.Constants.Companion.QUICK_PAY_QR
import com.uxi.bambupaymerchant.utils.Constants.Companion.SCAN_PAY_QR
import com.uxi.bambupaymerchant.utils.Constants.Companion.SEND_MONEY
import com.uxi.bambupaymerchant.utils.Utils
import com.uxi.bambupaymerchant.utils.convertTimeToDate
import kotlinx.android.synthetic.main.item_transaction.view.*
import org.apache.commons.lang3.builder.EqualsBuilder

/**
 * Created by Eraño Payawal on 11/18/20.
 * hunterxer31@gmail.com
 */
class RecentHistoryAdapter(private val activity: FragmentActivity?,
                           private val onHistoryItemClick: (Transaction) -> Unit
) : ListAdapter<Transaction, RecentHistoryAdapter.ViewHolder>(DiffCallback) {

    private var utils: Utils? = null
    init {
        utils = Utils(activity)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false),
        onHistoryItemClick
    )
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    inner class ViewHolder(
        itemView: View,
        private val onHistoryItemClick: (Transaction) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(transaction: Transaction?) {
            transaction?.let {
                when (it.transactionType) {
                    SEND_MONEY -> {
                        itemView.txt_transaction_type.text = activity?.getString(R.string.send_money)
                        if (it.balanceType == DEBIT) {
                            activity?.let { it1 ->
                                itemView.txt_amount.setTextColor((ContextCompat.getColor(it1, R.color.red)))
                            }
                        }
                    }
                    CASH_IN -> {
                        itemView.txt_transaction_type.text = activity?.getString(R.string.cash_in)
                        activity?.let { it1 ->
                            itemView.txt_amount.setTextColor((ContextCompat.getColor(it1, R.color.light_green)))
                        }
                    }
                    CASH_OUT -> itemView.txt_transaction_type.text = activity?.getString(R.string.cash_out)
                    SCAN_PAY_QR -> itemView.txt_transaction_type.text = activity?.getString(R.string.scan_pay)
                    CREATE_SCAN_QR -> itemView.txt_transaction_type.text = activity?.getString(R.string.create_scan_qr)
                    QUICK_PAY_QR -> itemView.txt_transaction_type.text = activity?.getString(R.string.quick_qr)
                    INCOME_SHARES -> itemView.txt_transaction_type.text = activity?.getString(R.string.income_shares)
                    else -> {itemView.txt_transaction_type.text = it.transactionType}
                }
                val transactionAmount = it.amount?.let { it1 -> utils?.currencyFormat(it1) }
                itemView.txt_amount.text = activity?.getString(R.string.ph_currency, transactionAmount)
                val dateTime = convertTimeToDate(it.dateCreated)
                itemView.txt_date.text = dateTime
            }
            itemView.container_history_item.setOnClickListener {
                onHistoryItemClick(transaction!!)
            }
        }
    }
    object DiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean =
            EqualsBuilder.reflectionEquals(oldItem, newItem)
    }

}