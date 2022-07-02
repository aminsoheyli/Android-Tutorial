package com.aminsoheyli.mvvmbasics

fun main(args: Array<String>) {
    val firstPerson = Person("Amin", 23, "Computer Architecture")
    val secondPerson = Person("Mohammad", 21, "Constructor")
    var i = 2
    fun sq() = (i * i).also { i++ }
    println(sq())
    val olderPerson = if (firstPerson.age >= secondPerson.age) firstPerson else secondPerson
    olderPerson.printPerson()

    /** RUN */
    run {
        if (firstPerson.age >= secondPerson.age) firstPerson else secondPerson
    }.printPerson()

    /** WITH */
    with(firstPerson) {
        age += 1
        "Age is now $age"
    }.println()

    /** CLASS RUN */
    firstPerson.run {
        age += 1
        "Age is now $age"
    }.println()

    /** CLASS LET */
    firstPerson.let { modifiedPerson ->
        modifiedPerson.age += 1
        "Age is now ${modifiedPerson.age}"
    }.println()

    secondPerson.apply {
        age += 1
        job = "Bus Driver"
    }.printPerson()
    secondPerson.also {
        it.age += 1
        it.job = "Hot-Dog Seller"
    }.printPerson()


//    println("2114889273")
//    println(firstPerson.hashCode())
}

fun String.println() = println(this)

data class Person(
    var name: String,
    var age: Int,
    var job: String
) {
    fun printPerson() = println(this.toString())

    //    override fun toString(): String {
//        return "Person(name=$name, age=$age, job=$job)"
//    }
}