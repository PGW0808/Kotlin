package com.kotlin_assignment.a7minuteworkout

object Constants {

    fun defaultExerciseList(): ArrayList<ExerciseModel>{
        val exerciseList = ArrayList<ExerciseModel>()
        val jumpingJacks = ExerciseModel(
            1,
            "Jumping Jacks",
            R.drawable.jumping_jacks,
            false,
            false
        )
        exerciseList.add(jumpingJacks)

        val wallSit = ExerciseModel(
            2,
            "Wall Sit",
            R.drawable.wallsit,
            false,
            false
        )
        exerciseList.add(wallSit)

        val pushUps = ExerciseModel(
            3,
            "Push Ups",
            R.drawable.pushups,
            false,
            false
        )
        exerciseList.add(pushUps)

        val abdominalCrunches = ExerciseModel(
            4,
            "Abdominal Crunch",
            R.drawable.cross_crunches,
            false,
            false
        )
        exerciseList.add(abdominalCrunches)

        val tricepsWithChair = ExerciseModel(
            5,
            "Triceps with Chair",
            R.drawable.triceps_with_chair,
            false,
            false
        )
        exerciseList.add(tricepsWithChair)

        val squats = ExerciseModel(
            6,
            "Squats",
            R.drawable.squat,
            false,
            false
        )
        exerciseList.add(squats)

        var sitUpsWithChair = ExerciseModel(
            7,
            "Cross Sit Ups",
            R.drawable.situps,
            false,
            false
        )
        exerciseList.add(sitUpsWithChair)

        val plank = ExerciseModel(
            8,
            "Plank",
            R.drawable.plank,
            false,
            false
        )
        exerciseList.add(plank)

        val runningOnPlace = ExerciseModel(
            9,
            "High Knee Running",
            R.drawable.high_knee_running,
            false,
            false
        )
        exerciseList.add(runningOnPlace)

        val lunges = ExerciseModel(
            10,
            "Lunges",
            R.drawable.lunges_elevated,
            false,
            false
        )
        exerciseList.add(lunges)

        val pushUpWithRotation = ExerciseModel(
            11,
            "Push Ups Rotation",
            R.drawable.pushuprotation,
            false,
            false
        )
        exerciseList.add(pushUpWithRotation)

        val sidePlank = ExerciseModel(
            12,
            "Side Plank",
            R.drawable.side_plank,
            false,
            false
        )
        exerciseList.add(sidePlank)

        return exerciseList
    }
}