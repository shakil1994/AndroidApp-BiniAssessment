package com.shakil.biniassessment.Interface

import com.shakil.biniassessment.Model.ModelBankList

interface IBankListCallbackListener {
    fun onBankListLoadSuccess(bankList: List<ModelBankList>)
    fun onBankListLoadFailed(message: String)
}