package com.shakil.biniassessment

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.shakil.biniassessment.Adapter.BankListAdapter
import com.shakil.biniassessment.Adapter.TransactionListAdapter
import com.shakil.biniassessment.Common.Common
import com.shakil.biniassessment.Model.ModelBankList
import com.shakil.biniassessment.Model.ModelSendMoney
import com.shakil.biniassessment.ViewModel.MainActivityViewModel
import com.shakil.biniassessment.ViewModel.SendMoneyActivityViewModel
import com.shakil.biniassessment.databinding.ActivityMainBinding
import com.shakil.biniassessment.databinding.ActivitySendMoneyBinding
import dmax.dialog.SpotsDialog

class SendMoneyActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendMoneyBinding
    private lateinit var sendMoneyActivityViewModel: SendMoneyActivityViewModel
    private lateinit var dialog: AlertDialog
    private var adapter: TransactionListAdapter? = null
    var layoutManager: LinearLayoutManager? = null

    internal var sendMoneyModel: List<ModelSendMoney> = ArrayList<ModelSendMoney>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendMoneyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sendMoneyActivityViewModel = ViewModelProvider(this)[SendMoneyActivityViewModel::class.java]

        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()
        dialog.show()

        binding.imgBack.setOnClickListener {
            finish()
        }

        sendMoneyActivityViewModel.getMessageError().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        transactionList()

        binding.sendMoney.setOnClickListener {
            sendMoneyDialog()
        }
    }

    private fun transactionList() {
        sendMoneyActivityViewModel.getTransactionList().observe(this, Observer {
            layoutManager = LinearLayoutManager(this)
            sendMoneyModel = it
            binding.recyclerTransactionList!!.layoutManager = layoutManager
            binding.recyclerTransactionList.setHasFixedSize(true)
            adapter = TransactionListAdapter(this, sendMoneyModel)
            adapter!!.notifyDataSetChanged()
            binding.recyclerTransactionList.adapter = adapter

            dialog.dismiss()
        })
    }

    private fun sendMoneyDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Send Money")
        builder.setMessage("Please fill information")

        val itemView = LayoutInflater.from(this).inflate(R.layout.layout_send_money, null)
        val edtBankName = itemView.findViewById<View>(R.id.edtBankName) as AppCompatEditText
        val edtBranchName = itemView.findViewById<View>(R.id.edtBranchName) as AppCompatEditText
        val edtAmount = itemView.findViewById<View>(R.id.edtAmount) as AppCompatEditText
        val edtToBankName = itemView.findViewById<View>(R.id.edtToBankName) as AppCompatEditText
        val edtToBranchName = itemView.findViewById<View>(R.id.edtToBranchName) as AppCompatEditText

        builder.setNegativeButton("CANCEL") { dialog, _ -> dialog.dismiss() }
        builder.setPositiveButton("SEND") { dialog, _ ->

            val sendMoney = ModelSendMoney()
            sendMoney.bankName = edtBankName.text.toString()
            sendMoney.branchName = edtBranchName.text.toString()
            sendMoney.amount = edtAmount.text.toString()
            sendMoney.toBankName = edtToBankName.text.toString()
            sendMoney.toBranchName = edtToBranchName.text.toString()

            if (sendMoney.bankName!!.isEmpty() || sendMoney.branchName!!.isEmpty()
                || sendMoney.amount!!.isEmpty() || sendMoney.toBankName!!.isEmpty() || sendMoney.toBranchName!!.isEmpty()){
                Toast.makeText(this, "Please fill information", Toast.LENGTH_SHORT).show()
            }
            else {
                sendMoneyToOtherBank(sendMoney)
            }
        }

        builder.setView(itemView)
        val updateDialog = builder.create()
        updateDialog.show()

    }

    private fun sendMoneyToOtherBank(sendMoney: ModelSendMoney) {
        dialog.show()
        FirebaseDatabase.getInstance()
            .getReference(Common.BANK_TRANACTION_REF)
            .push()
            .setValue(sendMoney)
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error Add Info",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnSuccessListener { task ->
                dialog.dismiss()
                sendMoneyActivityViewModel.loadTransactionList()
                Toast.makeText(this, "Transaction Successfully", Toast.LENGTH_SHORT).show()
            }
    }
}