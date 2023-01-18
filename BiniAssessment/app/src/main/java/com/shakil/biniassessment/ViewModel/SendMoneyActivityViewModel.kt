package com.shakil.biniassessment.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shakil.biniassessment.Common.Common
import com.shakil.biniassessment.Interface.ITransactionListCallbackListener
import com.shakil.biniassessment.Model.ModelBankList
import com.shakil.biniassessment.Model.ModelSendMoney

class SendMoneyActivityViewModel: ViewModel(), ITransactionListCallbackListener {

    private var transactionListMutable: MutableLiveData<List<ModelSendMoney>>? = null
    private var messageError: MutableLiveData<String> = MutableLiveData()
    private var transactionListCallbackListener: ITransactionListCallbackListener

    init {
        transactionListCallbackListener = this
    }

    override fun onTransactionListLoadSuccess(transactionList: List<ModelSendMoney>) {
        transactionListMutable!!.value = transactionList
    }

    override fun onTransactionListLoadFailed(message: String) {
        messageError.value = message
    }

    fun getTransactionList(): MutableLiveData<List<ModelSendMoney>>{

        if (transactionListMutable == null){
            transactionListMutable = MutableLiveData()
            loadTransactionList()
        }

        return transactionListMutable!!
    }

    fun getMessageError(): MutableLiveData<String>{
        return messageError
    }

    fun loadTransactionList() {
        val tempList = ArrayList<ModelSendMoney>()
        val transactionListRef = Firebase.database.getReference(Common.BANK_TRANACTION_REF)
        transactionListRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children){
                    val model = itemSnapshot.getValue<ModelSendMoney>(ModelSendMoney::class.java)
                    model!!.bankId = itemSnapshot.key
                    tempList.add(model)
                }
                transactionListCallbackListener.onTransactionListLoadSuccess(tempList)
            }

            override fun onCancelled(error: DatabaseError) {
                transactionListCallbackListener.onTransactionListLoadFailed(error.message)
            }

        })
    }
}