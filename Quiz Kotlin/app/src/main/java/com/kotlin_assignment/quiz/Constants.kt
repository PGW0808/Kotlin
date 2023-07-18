package com.kotlin_assignment.quiz

object Constants {

    const val USER_NAME : String = "user_name"
    const val TOTAL_QUESTIONS : String = "total_questions"
    const val CORRECT_QUESTIONS : String = "correct_questions"

    fun getQuestion():ArrayList<Question>{
        val questionList = ArrayList<Question>()

        val que1 = Question(
            1,"What country does this flag belong to?",
            R.drawable.ic_flag_of_argentina,
            "Argentina","Australia",
            "Austria", "Brazil",
            1
        )
        questionList.add(que1)

        val que2 = Question(
            2,"What country does this flag belong to?",
            R.drawable.ic_flag_of_australia,
            "Argentina","Australia",
            "Austria", "Brazil",
            2
        )
        questionList.add(que2)

        val que3 = Question(
            3,"What country does this flag belong to?",
            R.drawable.ic_flag_of_belgium,
            "Belgium","Australia",
            "Austria", "Brazil",
            1
        )
        questionList.add(que3)

        val que4 = Question(
            4,"What country does this flag belong to?",
            R.drawable.ic_flag_of_brazil,
            "Argentina","Australia",
            "Austria", "Brazil",
            4
        )
        questionList.add(que4)

        val que5 = Question(
            5,"What country does this flag belong to?",
            R.drawable.ic_flag_of_denmark,
            "Argentina","Australia",
            "Denmark", "Brazil",
            3
        )
        questionList.add(que5)

        val que6 = Question(
            6,"What country does this flag belong to?",
            R.drawable.ic_flag_of_fiji,
            "Argentina","Fiji",
            "Austria", "Brazil",
            2
        )
        questionList.add(que6)

        val que7 = Question(
            7,"What country does this flag belong to?",
            R.drawable.ic_flag_of_germany,
            "Argentina","Australia",
            "Austria", "Germany",
            4
        )
        questionList.add(que7)

        val que8 = Question(
            8,"What country does this flag belong to?",
            R.drawable.ic_flag_of_india,
            "India","Australia",
            "Austria", "Brazil",
            1
        )
        questionList.add(que8)

        val que9 = Question(
            9,"What country does this flag belong to?",
            R.drawable.ic_flag_of_kuwait,
            "Argentina","Kuwait",
            "Austria", "Brazil",
            2
        )
        questionList.add(que9)

        val que10 = Question(
            10,"What country does this flag belong to?",
            R.drawable.ic_flag_of_new_zealand,
            "Argentina","Australia",
            "Austria", "New Zealand",
            4
        )
        questionList.add(que10)


        return  questionList
    }
}