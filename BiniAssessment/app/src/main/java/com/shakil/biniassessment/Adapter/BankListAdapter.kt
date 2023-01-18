package com.shakil.biniassessment.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.shakil.biniassessment.Model.ModelBankList
import com.shakil.biniassessment.R

class BankListAdapter(var context: Context, var bankModelList: List<ModelBankList>): RecyclerView.Adapter<BankListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_bank_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtBankName.text = StringBuilder("Bank Name: ").append(bankModelList[position].bankName)
        holder.txtBranchName.text = StringBuilder("Branch Name: ").append(bankModelList[position].branchName)
        holder.txtRoutingNumber.text = StringBuilder("Routing Number: ").append(bankModelList[position].routingNumber)
    }

    override fun getItemCount(): Int {
        return bankModelList.size
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var txtBankName: AppCompatTextView
        var txtBranchName: AppCompatTextView
        var txtRoutingNumber: AppCompatTextView

        init {
            txtBankName = itemView.findViewById(R.id.txtBankName)
            txtBranchName = itemView.findViewById(R.id.txtBranchName)
            txtRoutingNumber = itemView.findViewById(R.id.txtRoutingNumber)
        }
    }
}