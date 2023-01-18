package com.shakil.biniassessment.DataValidator

object Validator {
    fun validateInput(bankName: String, branchName: String, routingNumber: String): Boolean{
        return !(bankName.isEmpty() || branchName.isEmpty() || routingNumber.isEmpty())
    }
}