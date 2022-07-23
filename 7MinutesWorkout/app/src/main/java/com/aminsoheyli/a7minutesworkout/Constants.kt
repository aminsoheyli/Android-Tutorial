package com.aminsoheyli.a7minutesworkout

object Constants {
    fun defaultExerciseList(): ArrayList<ExerciseModel> {
        val list = ArrayList<ExerciseModel>()
        list.add(ExerciseModel(1, "Jumping Jacks", R.drawable.ic_jumping_jacks))
        list.add(ExerciseModel(2, "Wall Sit", R.drawable.ic_wall_sit))
        list.add(ExerciseModel(3, "Push Up", R.drawable.ic_push_up))
        list.add(ExerciseModel(4, "Abdominal Crunch", R.drawable.ic_abdominal_crunch))
        list.add(ExerciseModel(5, "Step-Up onto Chair", R.drawable.ic_step_up_onto_chair))
        list.add(ExerciseModel(6, "Squat", R.drawable.ic_squat))
        list.add(ExerciseModel(7, "Tricep Dip On Chair", R.drawable.ic_triceps_dip_on_chair))
        list.add(ExerciseModel(8, "Plank", R.drawable.ic_plank))
        list.add(
            ExerciseModel(
                9,
                "High Knees Running In Place",
                R.drawable.ic_high_knees_running_in_place
            )
        )
        list.add(ExerciseModel(10, "Lunges", R.drawable.ic_lunge))
        list.add(ExerciseModel(11, "Push up and Rotation", R.drawable.ic_push_up_and_rotation))
        list.add(ExerciseModel(12, "Side Plank", R.drawable.ic_side_plank))
        return list
    }
}