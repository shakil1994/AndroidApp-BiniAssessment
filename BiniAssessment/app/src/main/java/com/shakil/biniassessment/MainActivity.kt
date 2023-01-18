package com.shakil.biniassessment

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.shakil.biniassessment.Adapter.BankListAdapter
import com.shakil.biniassessment.Common.Common
import com.shakil.biniassessment.Common.MySwipeHelper
import com.shakil.biniassessment.Interface.IMyButtonCallback
import com.shakil.biniassessment.Model.ModelBankList
import com.shakil.biniassessment.ViewModel.MainActivityViewModel
import com.shakil.biniassessment.databinding.ActivityMainBinding
import dmax.dialog.SpotsDialog

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var dialog: AlertDialog
    private var adapter: BankListAdapter? = null
    var layoutManager: LinearLayoutManager? = null

    internal var bankListModel: List<ModelBankList> = ArrayList<ModelBankList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()
        dialog.show()

        mainActivityViewModel.getMessageError().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        bankList()

        swipeButton()

        binding.addBank.setOnClickListener {
            addBankDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nearByBank){
            startActivity(Intent(this, NearByBankActivity::class.java))
        }

        else if (item.itemId == R.id.sendMoney){
            startActivity(Intent(this, SendMoneyActivity::class.java))
        }

        return super.onOptionsItemSelected(item)

    }

    private fun addBankDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Add Bank Info")
        builder.setMessage("Please fill information")

        val itemView = LayoutInflater.from(this).inflate(R.layout.layout_add_bank_item, null)
        val edtBankName = itemView.findViewById<View>(R.id.edtBankName) as AppCompatEditText
        val edtBranchName = itemView.findViewById<View>(R.id.edtBranchName) as AppCompatEditText
        val edtRoutingNumber =
            itemView.findViewById<View>(R.id.edtRoutingNumber) as AppCompatEditText

        builder.setNegativeButton("CANCEL") { dialog, _ -> dialog.dismiss() }
        builder.setPositiveButton("CREATE") { dialog, _ ->

            val addBank = ModelBankList()
            addBank.bankName = edtBankName.text.toString()
            addBank.branchName = edtBranchName.text.toString()
            addBank.routingNumber = edtRoutingNumber.text.toString()

            if (addBank.bankName!!.isEmpty() || addBank.branchName!!.isEmpty() || addBank.routingNumber!!.isEmpty()){
                Toast.makeText(this, "Please fill information", Toast.LENGTH_SHORT).show()
            }
            else {
                addBankList(addBank)
            }
        }

        builder.setView(itemView)
        val updateDialog = builder.create()
        updateDialog.show()

    }

    private fun addBankList(addBank: ModelBankList) {
        dialog.show()
        FirebaseDatabase.getInstance()
            .getReference(Common.BANK_LIST_REF)
            .push()
            .setValue(addBank)
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error Add Info",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnSuccessListener { task ->
                dialog.dismiss()
                mainActivityViewModel.loadBankList()
                Toast.makeText(this, "Bank Added Successfully", Toast.LENGTH_SHORT).show()
            }
    }


    private fun swipeButton() {
        val swipe = object : MySwipeHelper(this, binding.recyclerBankList, 200) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {

                buffer.add(
                    MyButton(this@MainActivity, "Delete",
                        30, 0, Color.parseColor("#333639"),
                        object : IMyButtonCallback {
                            override fun onClick(pos: Int) {
                                Common.bankSelected = bankListModel[pos]
                                showDeleteDialog()
                            }
                        })
                )

                buffer.add(
                    MyButton(this@MainActivity, "Update",
                        30, 0, Color.parseColor("#560027"),
                        object : IMyButtonCallback {
                            override fun onClick(pos: Int) {
                                Common.bankSelected = bankListModel[pos]
                                showUpdateDialog()
                            }
                        })
                )
            }

        }
    }

    private fun showDeleteDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Delete Bank")
        builder.setMessage("Do you really want to delete this bank?")

        builder.setNegativeButton("CANCEL") { dialog, _ -> dialog.dismiss() }
        builder.setPositiveButton("DELETE") { dialog, _ ->
            deleteBank()
        }

        val deleteDialog = builder.create()
        deleteDialog.show()
    }

    private fun deleteBank() {
        FirebaseDatabase.getInstance()
            .getReference(Common.BANK_LIST_REF)
            .child(Common.bankSelected!!.bankId!!)
            .removeValue()
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error Update Info",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnSuccessListener { task ->
                mainActivityViewModel.loadBankList()
                Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showUpdateDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Update Bank Info")
        builder.setMessage("Please fill information")

        val itemView = LayoutInflater.from(this).inflate(R.layout.layout_update_bank_item, null)
        val edtBankName = itemView.findViewById<View>(R.id.edtBankName) as AppCompatEditText
        val edtBranchName = itemView.findViewById<View>(R.id.edtBranchName) as AppCompatEditText
        val edtRoutingNumber =
            itemView.findViewById<View>(R.id.edtRoutingNumber) as AppCompatEditText

        edtBankName.setText(Common.bankSelected!!.bankName)
        edtBranchName.setText(Common.bankSelected!!.branchName)
        edtRoutingNumber.setText(Common.bankSelected!!.routingNumber)

        builder.setNegativeButton("CANCEL") { dialog, _ -> dialog.dismiss() }
        builder.setPositiveButton("UPDATE") { dialog, _ ->
            val updateData = HashMap<String, Any>()
            updateData["bankName"] = edtBankName.text.toString()
            updateData["branchName"] = edtBranchName.text.toString()
            updateData["routingNumber"] = edtRoutingNumber.text.toString()

            updateBank(updateData)
        }

        builder.setView(itemView)
        val updateDialog = builder.create()
        updateDialog.show()
    }

    private fun updateBank(updateData: java.util.HashMap<String, Any>) {
        FirebaseDatabase.getInstance()
            .getReference(Common.BANK_LIST_REF)
            .child(Common.bankSelected!!.bankId!!)
            .updateChildren(updateData)
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error Update Info",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnSuccessListener { task ->
                mainActivityViewModel.loadBankList()
                Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show()
            }
    }

    private fun bankList() {
        mainActivityViewModel.getBankList().observe(this, Observer {
            layoutManager = LinearLayoutManager(this)
            bankListModel = it
            binding.recyclerBankList!!.layoutManager = layoutManager
            binding.recyclerBankList.setHasFixedSize(true)
            adapter = BankListAdapter(this, bankListModel)
            adapter!!.notifyDataSetChanged()
            binding.recyclerBankList.adapter = adapter

            dialog.dismiss()
        })
    }
}