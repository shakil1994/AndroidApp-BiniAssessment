package com.shakil.biniassessment.Interface

import com.shakil.biniassessment.Model.ModelSendMoney

interface ITransactionListCallbackListener {
    fun onTransactionListLoadSuccess(transactionList: List<ModelSendMoney>)
    fun onTransactionListLoadFailed(message: String)
}