package com.kotlin_assignment.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

     var bgExeBtn : Button = findViewById(R.id.coroutinBtn)
        bgExeBtn.setOnClickListener{
            lifecycleScope.launch {

            execute("Task Executed Successfully!")
            }
        }
    }

    private suspend fun execute(result: String){

        withContext(Dispatchers.IO){
            for (i in 1..5000000){
                Log.e("delay : ", ""+1)
            }

            runOnUiThread {

                Toast.makeText(this@MainActivity, result, Toast.LENGTH_LONG).show()
            }

        }


    }
}