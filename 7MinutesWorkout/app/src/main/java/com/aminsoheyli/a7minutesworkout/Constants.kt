package com.aminsoheyli.a7minutesworkout

object Constants {
    fun defaultExerciseList(): ArrayList<Exercise> {
        val list = ArrayList<Exercise>()
        list.add(Exercise(1, "Jumping Jacks", R.drawable.ic_jumping_jacks))
        list.add(Exercise(2, "Wall Sit", R.drawable.ic_wall_sit))
        list.add(Exercise(3, "Push Up", R.drawable.ic_push_up))
        list.add(Exercise(4, "Abdominal Crunch", R.drawable.ic_abdominal_crunch))
        list.add(Exercise(5, "Step-Up onto Chair", R.drawable.ic_step_up_onto_chair))
        list.add(Exercise(6, "Squat", R.drawable.ic_squat))
        list.add(Exercise(7, "Tricep Dip On Chair", R.drawable.ic_triceps_dip_on_chair))
        list.add(Exercise(8, "Plank", R.drawable.ic_plank))
        list.add(
            Exercise(
                9,
                "High Knees Running In Place",
                R.drawable.ic_high_knees_running_in_place
            )
        )
        list.add(Exercise(10, "Lunges", R.drawable.ic_lunge))
        list.add(Exercise(11, "Push up and Rotation", R.drawable.ic_push_up_and_rotation))
        list.add(Exercise(12, "Side Plank", R.drawable.ic_side_plank))
        return list
    }
}