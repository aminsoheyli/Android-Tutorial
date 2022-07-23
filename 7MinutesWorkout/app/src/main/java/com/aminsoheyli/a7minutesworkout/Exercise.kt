package com.aminsoheyli.a7minutesworkout

class Exercise(
    var id: Int,
    var name: String,
    var image: Int,
    var status: Status = Status.NONE
) {
    enum class Status {
        SELECTED,
        COMPLETED,
        NONE
    }
}
