package com.aminsoheyli.a7minutesworkout

object Constants {
    fun defaultExerciseList(): ArrayList<ExerciseModel> {
        val list = ArrayList<ExerciseModel>()
        list.add(ExerciseModel("Jumping Jacks", R.drawable.ic_jumping_jacks))
        list.add(ExerciseModel("Wall Sit", R.drawable.ic_wall_sit))
        list.add(ExerciseModel("Push Up", R.drawable.ic_push_up))
        list.add(ExerciseModel("Abdominal Crunch", R.drawable.ic_abdominal_crunch))
        list.add(ExerciseModel("Step-Up onto Chair", R.drawable.ic_step_up_onto_chair))
        list.add(ExerciseModel("Squat", R.drawable.ic_squat))
        list.add(ExerciseModel("Tricep Dip On Chair", R.drawable.ic_triceps_dip_on_chair))
        list.add(ExerciseModel("Plank", R.drawable.ic_plank))
        list.add(
            ExerciseModel("High Knees Running In Place", R.drawable.ic_high_knees_running_in_place)
        )
        list.add(ExerciseModel("Lunges", R.drawable.ic_lunge))
        list.add(ExerciseModel("Push up and Rotation", R.drawable.ic_push_up_and_rotation))
        list.add(ExerciseModel("Side Plank", R.drawable.ic_side_plank))
        return list
    }
}

fun main() {
    for(item in Constants.defaultExerciseList())
        print(item.id)
}