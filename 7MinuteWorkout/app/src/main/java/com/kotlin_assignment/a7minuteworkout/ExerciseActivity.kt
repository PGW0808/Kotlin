package com.kotlin_assignment.a7minuteworkout

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import com.kotlin_assignment.a7minuteworkout.databinding.ActivityExerciseBinding
import java.lang.Exception
import java.util.Locale

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding : ActivityExerciseBinding? = null

    private var restTimer : CountDownTimer? = null
    private var restProgress = 0

    private var exerciseTimer : CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarExercise)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this, this)

        binding?.toolBarExercise?.setNavigationOnClickListener{
            onBackPressed()
        }

        setUpRestView()
    }

    private fun setUpRestView(){

        try {
            val soundURI = Uri.parse("android.resource://com.kotlin_assignment.a7minuteworkout/" + R.raw.boxing_bell_single)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        }catch (e : Exception){
            e.printStackTrace()
        }

        binding?.flRestProgressBar?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExercise?.visibility = View.INVISIBLE
        binding?.flExerciseProgressBar?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.tvUpcomingExeName?.visibility = View.VISIBLE
        binding?.tvUpcomingExeLabel?.visibility = View.VISIBLE

         if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        binding?.tvUpcomingExeName?.text = exerciseList!![currentExercisePosition + 1].getName()
        setRstProgressBar()
    }

    private fun setUpExerciseView(){

        binding?.flRestProgressBar?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExercise?.visibility = View.VISIBLE
        binding?.flExerciseProgressBar?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.tvUpcomingExeName?.visibility = View.INVISIBLE
        binding?.tvUpcomingExeLabel?.visibility = View.INVISIBLE

         if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExercise?.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()
    }

    private fun setRstProgressBar(){
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(10000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++

                setUpExerciseView()
            }


        }.start()
    }

    private fun setExerciseProgressBar(){
        binding?.progressBar?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(30000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.exerciseProgressBar?.progress = 30 - exerciseProgress
                binding?.tvExerciseTimer?.text = (30 - exerciseProgress).toString()

                if (millisUntilFinished <= 6000){
                    val remainigSeconds = ((millisUntilFinished / 1000)).toInt()
                    speakOut("$remainigSeconds")
                }
            }

            override fun onFinish() {
                if (currentExercisePosition < exerciseList?.size!! - 1){
                    setUpRestView()
                    speakOut("Take Rest for 10 seconds and Be Ready")
                }else{
                    speakOut("Congratulations! You have completed 7 minutes workout")
                    //Toast.makeText(this@ExerciseActivity, "Congratulations! You have completed 7 minutes workout.", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        if(tts != null){
             tts?.stop()
            tts?.shutdown()
        }
        if(player != null){
            player!!.stop()
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