package com.jnfran92.kotcoin.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jnfran92.kotcoin.R
import com.jnfran92.model.data.crypto.Crypto
import kotlinx.android.synthetic.main.view_crypto_item.view.*
import javax.inject.Inject

/**
 * Adapter for Crypto data and RecyclerView UI Element
 */
class CryptoListAdapter @Inject constructor(private val context: Context)
    :RecyclerView.Adapter<CryptoListAdapter.CryptoViewHolder>(){


    private var cryptoList: ArrayList<Crypto> = ArrayList()
    private val layoutInflater: LayoutInflater =
        this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val view = this.layoutInflater.inflate(R.layout.view_crypto_item, parent, false)
        return CryptoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cryptoList.size
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val crypto = cryptoList[position]
        holder.itemName.text = crypto.name
        holder.itemSymbol.text = crypto.symbol

        val price = crypto.quoteEntity.usd.price.toString()
        holder.itemPrice.text = "$ $price"

        val marketCap = crypto.quoteEntity.usd.marketCap.toString()
        holder.itemMarketCap.text = "$ $marketCap"

        holder.itemLastUpdate.text = crypto.quoteEntity.usd.lastUpdated

    }

    fun setData(data: ArrayList<Crypto>){
        this.cryptoList = data
        this.notifyDataSetChanged()
    }

    class CryptoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemName :TextView = itemView.tv_cryptoItem_name
        val itemSymbol :TextView = itemView.tv_cryptoItem_name
        val itemPrice :TextView = itemView.tv_cryptoItem_name
        val itemMarketCap :TextView = itemView.tv_cryptoItem_name
        val itemLastUpdate: TextView = itemView.tv_cryptoItem_lastUpdate
    }
}