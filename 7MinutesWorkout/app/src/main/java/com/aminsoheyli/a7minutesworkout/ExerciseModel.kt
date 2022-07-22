package com.aminsoheyli.a7minutesworkout

class ExerciseModel(
    var name: String,
    var image: Int,
    var isCompleted: Boolean = false,
    var isSelected: Boolean = false
) {
    var id = ++idCounter

    companion object {
        private var idCounter = 0
    }
}