package com.shakil.biniassessment.Common

import com.shakil.biniassessment.Model.ModelBankList
import com.shakil.biniassessment.Network.IGoogleAPIService
import com.shakil.biniassessment.Network.RetrofitClient

object Common {
    const val BANK_TRANACTION_REF: String = "Transaction"
    const val BANK_LIST_REF: String = "Category"
    var bankSelected: ModelBankList? = null

    private val GOOGLE_API_URL = "https://maps.googleapis.com/"

    val googleApiService: IGoogleAPIService
        get() = RetrofitClient.getRetrofit(GOOGLE_API_URL).create(IGoogleAPIService::class.java)
}