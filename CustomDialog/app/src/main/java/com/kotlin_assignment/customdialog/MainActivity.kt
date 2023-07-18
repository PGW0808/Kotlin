package com.kotlin_assignment.customdialog

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    var customProgressDialog: Dialog? = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val snakbarBtn: ImageButton = findViewById(R.id.star)
        snakbarBtn.setOnClickListener {view->
            Snackbar.make(view, "This is snakbar button.", Snackbar.LENGTH_LONG).show()
        }

         val btnAlertDialog: Button = findViewById(R.id.alertDialog)
            btnAlertDialog.setOnClickListener { view ->
                //Launch Alert Dialog
                alertDialogFunction()
            }
        val btnCustomAlertDialog: Button = findViewById(R.id.customDialog)
            btnCustomAlertDialog.setOnClickListener {
                //Launch Alert Dialog
                alertCustomDialogFunction()
            }
        val customProgressBar: Button = findViewById(R.id.customProgressDialog)
        customProgressBar.setOnClickListener{
            customProgessDialogFunction()
            lifecycleScope.launch{
                    execute("Task Done Successfully!")
            }
        }

    }

    private fun alertDialogFunction() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage("This is alert. This is alert dialog")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, which ->
            Toast.makeText(applicationContext, "Clicked Yes", Toast.LENGTH_LONG).show()
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){ dialogInterface, which ->
            Toast.makeText(applicationContext, "Clicked No", Toast.LENGTH_LONG).show()
            dialogInterface.dismiss()
        }
        builder.setNeutralButton("Cancle"){ dialogInterface, which ->
            Toast.makeText(applicationContext, "Clicked Cancle", Toast.LENGTH_LONG).show()
            dialogInterface.dismiss()
        }
        val alertDialog:AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun alertCustomDialogFunction() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom)
        customDialog.findViewById<TextView>(R.id.tv_submitBtn).setOnClickListener {
            Toast.makeText(applicationContext, "Clicked Submit", Toast.LENGTH_LONG).show()
            customDialog.dismiss()
        }
        customDialog.findViewById<TextView>(R.id.tv_cancleBtn).setOnClickListener {
            Toast.makeText(applicationContext, "Clicked Cancle", Toast.LENGTH_LONG).show()
            customDialog.dismiss()
        }
        customDialog.setCancelable(false)
        customDialog.show()

    }

    private suspend fun execute(result: String){

        withContext(Dispatchers.IO){
            for (i in 1..100000){
                Log.e("delay : ", ""+1)
            }

            runOnUiThread {
                cancleProgessDialog()
                Toast.makeText(this@MainActivity, result, Toast.LENGTH_LONG).show()
            }

        }


    }

    private fun cancleProgessDialog(){
        if(customProgressDialog != null){
            customProgressDialog?.dismiss()
            customProgressDialog = null
        }
    }

    private fun customProgessDialogFunction(){
         customProgressDialog = Dialog(this)

        customProgressDialog?.setContentView(R.layout.dialog_custom_progress)

        customProgressDialog?.show()
    }
}