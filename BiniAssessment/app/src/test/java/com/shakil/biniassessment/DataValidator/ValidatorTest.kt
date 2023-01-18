package com.shakil.biniassessment.DataValidator

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ValidatorTest{

    @Test
    fun whenInputIsValid(){
        val bankName = "Some"
        val branchName = "Some"
        val routingNumber = "Some"

        val result = Validator.validateInput(bankName, branchName, routingNumber)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun whenInputIsInvalid(){
        val bankName = ""
        val branchName = ""
        val routingNumber = ""

        val result = Validator.validateInput(bankName, branchName, routingNumber)
        assertThat(result).isEqualTo(false)
    }
}
