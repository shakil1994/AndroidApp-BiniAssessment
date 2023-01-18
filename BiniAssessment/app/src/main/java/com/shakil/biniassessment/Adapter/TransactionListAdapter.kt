package com.shakil.biniassessment.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.shakil.biniassessment.Model.ModelBankList
import com.shakil.biniassessment.Model.ModelSendMoney
import com.shakil.biniassessment.R

class TransactionListAdapter(var context: Context, var transactionModelList: List<ModelSendMoney>): RecyclerView.Adapter<TransactionListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_transaction_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtToBankName.text = StringBuilder("Bank Name: ").append(transactionModelList[position].toBankName)
        holder.txtToBranchName.text = StringBuilder("Branch Name: ").append(transactionModelList[position].toBranchName)
        holder.txtAmount.text = StringBuilder("Amount: ").append(transactionModelList[position].amount).append(" TK")
    }

    override fun getItemCount(): Int {
        return transactionModelList.size
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var txtToBankName: AppCompatTextView
        var txtToBranchName: AppCompatTextView
        var txtAmount: AppCompatTextView

        init {
            txtToBankName = itemView.findViewById(R.id.txtToBankName)
            txtToBranchName = itemView.findViewById(R.id.txtToBranchName)
            txtAmount = itemView.findViewById(R.id.txtAmount)
        }
    }
}