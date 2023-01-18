package com.shakil.biniassessment.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shakil.biniassessment.Common.Common
import com.shakil.biniassessment.Interface.IBankListCallbackListener
import com.shakil.biniassessment.Model.ModelBankList

class MainActivityViewModel: ViewModel(), IBankListCallbackListener {
    private var bankListMutable: MutableLiveData<List<ModelBankList>>? = null
    private var messageError: MutableLiveData<String> = MutableLiveData()
    private var bankListCallbackListener: IBankListCallbackListener

    init {
        bankListCallbackListener = this
    }

    override fun onBankListLoadSuccess(bankList: List<ModelBankList>) {
        bankListMutable!!.value = bankList
    }

    override fun onBankListLoadFailed(message: String) {
        messageError.value = message
    }

    fun getBankList(): MutableLiveData<List<ModelBankList>>{

        if (bankListMutable == null){
            bankListMutable = MutableLiveData()
            loadBankList()
        }

        return bankListMutable!!
    }

    fun getMessageError(): MutableLiveData<String>{
        return messageError
    }

    fun loadBankList() {
        val tempList = ArrayList<ModelBankList>()
        val bankListRef = Firebase.database.getReference(Common.BANK_LIST_REF)
        bankListRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children){
                    val model = itemSnapshot.getValue<ModelBankList>(ModelBankList::class.java)
                    model!!.bankId = itemSnapshot.key
                    tempList.add(model)
                }
                bankListCallbackListener.onBankListLoadSuccess(tempList)
            }

            override fun onCancelled(error: DatabaseError) {
                bankListCallbackListener.onBankListLoadFailed(error.message)
            }

        })
    }
}