package com.kotlin_assignment.a7minuteworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import com.kotlin_assignment.a7minuteworkout.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding: ActivityMainBinding? = null
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding?.root)

        tts = TextToSpeech(this, this)

//        val fl_start_btn : FrameLayout = findViewById(R.id.fl_start)
        binding?.flStart?.setOnClickListener{
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
            speakOut("Lets start 7 minutes workout, Be Ready")
        }
    }

    override fun onDestroy(){
        super.onDestroy()

        if(tts != null){
             tts?.stop()
            tts?.shutdown()
        }

        binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.US)

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "Initialization Failed!")

            }else{
                Log.e("TTS", "The language specified is not supported!")

            }
        }
    }

    private fun speakOut(text:String){
        tts?.speak(text, TextToSpeech.QUEUE_ADD, null, "")
    }

}