package com.aminsoheyli.quizapp

object Constants {
    private val TEXT = "What country does this flag belong to?"
    fun getQuestions(): Array<Question> = arrayOf(
        Question(
            1, TEXT, R.drawable.ic_flag_of_argentina, arrayOf(
                "Argentina", "Australia",
                "Armenia", "Austria"
            ), 1
        ), Question(
            2, TEXT, R.drawable.ic_flag_of_australia, arrayOf(
                "Angola", "Austria",
                "Australia", "Armenia"
            ), 3
        ), Question(
            3, TEXT, R.drawable.ic_flag_of_brazil, arrayOf(
                "Belarus", "Belize",
                "Brunei", "Brazil"
            ), 4
        ), Question(
            4, TEXT, R.drawable.ic_flag_of_belgium, arrayOf(
                "Bahamas", "Belgium",
                "Barbados", "Belize"
            ), 2
        ), Question(
            5, TEXT, R.drawable.ic_flag_of_fiji, arrayOf(
                "Gabon", "France",
                "Fiji", "Finland"
            ), 3
        ), Question(
            6, TEXT, R.drawable.ic_flag_of_germany, arrayOf(
                "Germany", "Georgia",
                "Greece", "none of these"
            ), 1
        ), Question(
            7, TEXT, R.drawable.ic_flag_of_denmark, arrayOf(
                "Dominica", "Egypt",
                "Denmark", "Ethiopia"
            ), 3
        ), Question(
            8, TEXT, R.drawable.ic_flag_of_india, arrayOf(
                "Ireland", "Iran",
                "Hungary", "India"
            ), 4
        ), Question(
            9, TEXT, R.drawable.ic_flag_of_new_zealand, arrayOf(
                "Australia", "New Zealand",
                "Tuvalu", "United States of America"
            ), 2
        ), Question(
            10, TEXT, R.drawable.ic_flag_of_kuwait, arrayOf(
                "Kuwait", "Jordan",
                "Sudan", "Palestine"
            ), 1
        )
    )

}