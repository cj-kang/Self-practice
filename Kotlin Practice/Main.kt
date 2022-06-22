open class WizardingWorld
{
    constructor (fname: String, age: Int)
    //secondary constructor
    constructor (type: String?)
}

open class Wizards (fname: String, var age: Int) : WizardingWorld(fname, age)
{
    var name: String? = fname
    //nullable string
    fun personalData(): Boolean {
        return name == null
    }
}

class OtherCreatures (var type: String?, livingArea: String?, gender: String?) : WizardingWorld(type)
{
    var living: String? = livingArea
    var gen: String? = gender
    //nullable strings

    val fullType: String
        get() = "You are $type, living in $living, in $gen form"

    fun personalData(): Boolean {
        return living === null && gen ===null
    }

}

interface DarkMagic
{
    fun curse()
    fun attack(dangerLevel: Int)
}

open class DarkWizards (var wizard: Wizards, var wanted: Boolean, var countC: Int, var countA: Int): DarkMagic
{
    var yearInPrison: Int = 0
    var bounty: Double = 0.0

    //implementing interface methods
    override fun curse() {
        yearInPrison += 10*countC
        if(wanted === true) {
            bounty += 1000.50 * countC
        }
    }

    override fun attack(dangerLevel: Int) {
        if(dangerLevel <3) {
            yearInPrison += 3 * countA
        }else {
            yearInPrison += 5 * countA
        }
        if(wanted === true) {
            bounty += 500.0 * countA
        }
    }

    fun getTotalBounty(): Double{
        var totalBounty: Double = bounty

        return totalBounty
    }

}

interface Magic
{
    fun protect(number: Int)
    fun change(number: Int)
    fun fix(number: Int)
    fun control(number: Int)
}

abstract class OrdinaryWizards (wizard: Wizards) : Magic
{
    protected var level: Int = 0
    var magicLevel = level

    //implementing interface methods
    override fun protect(number: Int) {

        if (number == 0) {
            level = 0
        }

        level = when(number){
            in 1..3 -> 1
            in 4..6 -> 2
            else -> 3
        }
    }

    override fun change(number: Int) {
        if(number in 1..3){
            level += 1
        }else if (number in 4..10){
            level += 2
        }else if (number > 10){
            level +=3
        }
    }

    override fun fix(number: Int) {
        if(number in 1..3){
            level += 1
        }else if (number in 4..10){
            level += 2
        }else if (number > 10){
            level +=3
        }
    }

    override fun control(number: Int) {
        if(number > 3){
            level += 1
        }
    }
}

abstract class Hogwarts (wizard: Wizards) : DarkMagic, OrdinaryWizards(wizard)
{
    protected var housePoint: Int = 0
    protected var housePoints = mutableMapOf(Houses.Gryffindor to 0, Houses.Hufflepuff to 0, Houses.Ravenclaw to 0, Houses.Slytherin to 0)

    //Implementing interface methods
    override fun curse() {
        housePoint -= 300
    }

    override fun attack(dangerLevel: Int) {
        if(dangerLevel < 2) {
            housePoint -= 70
        }else {
            housePoint -= 150
        }
    }
}

enum class Houses
{
    Gryffindor,
    Slytherin,
    Hufflepuff,
    Ravenclaw
}

enum class LetterGrade
{
    O, E, A, P, D, T
}

open class Student (wizard: Wizards, var house: Houses, var year: Int) : Hogwarts (wizard)
{
    protected var averageGrade: Double = 0.0
    protected var graduationPoints: Double = 0.0
    protected var grades = mutableListOf<Int>()

    fun addElement(letter: LetterGrade): MutableList<Int> {
        when (letter) {
            LetterGrade.O -> grades.add(5)
            LetterGrade.E -> grades.add(4)
            LetterGrade.A -> grades.add(3)
            LetterGrade.P -> grades.add(2)
            LetterGrade.D -> grades.add(1)
            LetterGrade.T -> grades.add(0)
        }
        return grades
    }

    //Method overloading
    open fun addElement(): MutableMap<Houses, Int> {
        when (house) {
            Houses.Gryffindor -> housePoints.replace(Houses.Gryffindor, housePoint)
            Houses.Ravenclaw -> housePoints.replace(Houses.Ravenclaw, housePoint)
            Houses.Slytherin -> housePoints.replace(Houses.Slytherin, housePoint)
            Houses.Hufflepuff -> housePoints.replace(Houses.Hufflepuff, housePoint)
        }

        return housePoints
    }

    fun calAverageGrade(): Double {
        averageGrade = (grades.sum() / grades.count()).toDouble()

        return averageGrade
    }

    open fun calGraduationPossibility(level: Int): Double {
        if(level > 6){
            graduationPoints += 3
            graduationPoints +=
                if(averageGrade >= 3.0) {
                    2
                }else 1
        }else graduationPoints += 1

        return graduationPoints
    }

}

class Prefect (wizard: Wizards, house: Houses, year: Int) : Student (wizard, house, year)
{
    fun academicStanding () {
        if(averageGrade < 4.0) {
            var neededScore: Double = 4.0*(grades.count()+1)-grades.sum()
            println("Warning: Your academic standing is not in a good position")
            println("To reach averageGrade of 4.0, you need $neededScore more points.")
        }else {
            println("You are currently in good academic standing to remain as a Prefect.")
        }
    }

    //Method overriding from Student
    override fun calGraduationPossibility(level: Int): Double {
        super.calGraduationPossibility(level)
        graduationPoints += 1.5

        return graduationPoints
    }

}

class Professor (wizard: Wizards) : Hogwarts (wizard)
{
    //Method overriding from Hogwarts
    override fun attack(dangerLevel: Int) {
        super.attack(dangerLevel)
        if(dangerLevel < 2){
            println("You can give detention to this student. Decide his/her detention time and location.")
        }else {
            println("You can give a suspension to this student. Decide his/her suspension period.")
        }
    }

    override fun curse() {
        super.curse()
        println("You can expel this student. Host Disciplinary Committee.")
    }

    fun assignPrefect(student: Student){
        if (student.year > 4){
            if(student.calAverageGrade() > 4.5){
                println("This student satisfies the qualifications to be a prefect.")
            }else {
                println("This student does not have a good academic standing.")
            }
        } else {
            println("This student is too young to be a prefect.")
        }
    }

    fun chargeHousePoint(student: Student, goodAction: Boolean): MutableMap<Houses, Int> {

        if(goodAction) {
            housePoint += 20
        }else housePoint -= 10

        when (student.house) {
            Houses.Gryffindor -> housePoints.replace(Houses.Gryffindor, housePoint)
            Houses.Ravenclaw -> housePoints.replace(Houses.Ravenclaw, housePoint)
            Houses.Slytherin -> housePoints.replace(Houses.Slytherin, housePoint)
            Houses.Hufflepuff -> housePoints.replace(Houses.Hufflepuff, housePoint)
        }
        return housePoints
    }
}

class Summary
{
    //Overloading printOut() method

    //if the user is not a wizard
    fun printOut(otherCreatures: OtherCreatures){
        if(otherCreatures.personalData()) {
            println("You chose not to reveal your information.")
        }else {
            println(otherCreatures.fullType)
        }
    }

    //if the user is a wizard
    fun printOut (wizard: Wizards){
        var print : String = "You are ${wizard.age} years old Wizard."
        if(wizard.personalData()){
            println("$print.")
        }else {
            println("$print ${wizard.name}.")
        }
    }

    //if the user identifies him/herself as a dark wizard
    fun printOut (darkWizards: DarkWizards){
        if(darkWizards.wanted){
            println("You are currently in Wanted posters with the bounty of $${darkWizards.getTotalBounty()}.")
            println("You are charged for ${darkWizards.countA} times of illegal attack spells,")
            println("and ${darkWizards.countC} times of Unforgivable Curses.")
        }else {
            println("You are currently not in Wanted posters. However, you will be charged for: ")
            println("${darkWizards.countA} times of illegal attack spells, and ${darkWizards.countC} times of Unforgivable Curses.")
        }
    }

    //if the user is an ordinary wizard and is a student in Hogwarts
    fun printOut (student: Student, level: Int){
        println("Welcome to Hogwarts. You are in ${student.house} House.")
        println("Currently you are in Year ${student.year}.")
        println("Your magic level is: $level.")
        println("Your graduation point is ${student.calGraduationPossibility(level)}.")
        if(student.calGraduationPossibility(level) >= 5.0){
            println("You are eligible to graduate once you finish your final year.")
        }else{
            println("You still need "+(5-student.calGraduationPossibility(level))+" graduation points to be eligible.")
        }
    }

    //if the user is the student and selected as a prefect
    fun printOut(prefect: Prefect, level: Int){
        println("You are the Prefect of ${prefect.house} House, currently in Year ${prefect.year}.")
        prefect.academicStanding()
        println("Your magic level is $level")
        println("Your graduation point is ${prefect.calGraduationPossibility(level)}.")
        if(prefect.calGraduationPossibility(level) >= 5.0){
            println("You are eligible to graduate once you finish your final year.")
        }else{
            println("You still need "+(5-prefect.calGraduationPossibility(level))+" graduation points to be eligible.")
        }
    }

    //if the user is the professor
    fun printOut(professor: Professor, student: Student, attack: Boolean, curse: Boolean, dangerLevel: Int, goodAction: Boolean){
        println("You are the professor teaching in Hogwarts.")
        println("-------------------------------------------")
        println("You can assign ${student.house}'s Prefect.")
        professor.assignPrefect(student)
        println("-------------------------------------------")
        if(attack) {
            println("Student used attacking spells without supervision. Its danger level was $dangerLevel.")
            professor.attack(dangerLevel)
        }else if (curse){
            println("Student used Unforgivable Curse outside of Defense Against Dark Art class.")
            professor.curse()
        }else {
            professor.chargeHousePoint(student, goodAction)
        }
    }
}

fun main(args: Array<String>) {

    var summary: Summary = Summary()

    println("Please answer the following questions to decide your position in Wizarding World.")
    println("Are you a Wizard? (please type YES or NO)")
    val human = readLine()
    if(human == "NO"){

        //User is non-wizard

        println("Please enter your type.")
        val type: String = readLine()!!
        println("Please enter your living area and gender. If you do not wish to reveal them, please enter 'null' with a space.")
        val info = readLine()
        val area: String? = info?.split(" ")?.get(0)
        val gender: String? = info?.split(" ")?.get(1)
        var nonWizard = OtherCreatures(type, area, gender)

        summary.printOut(nonWizard)

    }else {

        //User is a wizard

        println("Please enter your firstname and age separated with a space. If you do not wish to reveal your name, please enter 'null'. The youngest possible age is 11.")
        val info = readLine()
        val name: String = info!!.split(" ")?.get(0)
        val age: Int = info!!.split(" ")[1].toInt()
        var wizard = Wizards(name, age)

        println("Do you identify yourself as a Dark Wizard? (Please answer YES or NO")
        val dark = readLine()
        if(dark == "YES"){

            // User identifies him/herself as a Dark Wizard

            println("Is your Wanted poster published? (Please answer YES or NO)")
            val wanted = readLine()
            var ifWanted: Boolean = true
            println("How many times did you cast attack spells to others? (Please enter a single number)")
            val attack = readLine()
            println("How many times did you cast Unforgivable Curses? (Please enter a single number)")
            val curse = readLine()
            if(wanted == "NO"){
                ifWanted = false
            }
            var darkWizards = DarkWizards(wizard, ifWanted, attack!!.toInt(), curse!!.toInt())

            summary.printOut(wizard)
            summary.printOut(darkWizards)
        }else {

            //User identifies him/herself as an Ordinary Wizard

            println("Are you a student attending Hogwarts? (Please answer YES or NO)")
            val stu = readLine()
            if(stu == "YES"){

                //User is a Hogwarts student

                println("Are you a Prefect? (Please answer YES or No)")
                val pre = readLine()
                println("Enter your house, year (1-6), and magic Level (0-10), separated by comma (,).")
                println("Houses are: Gryffindor, Slytherin, Hufflepuff, and Ravenclaw.")
                val stuInfo = readLine()
                var house = when(stuInfo!!.split(",")[0]){
                    Houses.Gryffindor.toString() -> Houses.Gryffindor
                    Houses.Hufflepuff.toString() -> Houses.Hufflepuff
                    Houses.Ravenclaw.toString() -> Houses.Ravenclaw
                    else -> Houses.Slytherin
                }
                var year = stuInfo.split(",")[1].toInt()
                var level = stuInfo.split(",")[2].toInt()
                println("Enter your letter grade collections (O, E, A, P, D, T) separated by comma (,): ")
                val grades = readLine()!!.split(",")
                var gradeList = mutableListOf<LetterGrade>()
                for(letterGrade in grades){
                        var letter = when (letterGrade) {
                            "O" -> LetterGrade.O
                            "E" -> LetterGrade.E
                            "A" -> LetterGrade.A
                            "P" -> LetterGrade.P
                            "D" -> LetterGrade.D
                            else -> LetterGrade.T
                        }
                        gradeList.add(letter)
                }
                var student = Student(wizard,house,year)

                for(j in 0..gradeList.size){
                    student.addElement(gradeList[j])
                }

                if(pre == "YES"){

                    //user is the prefect

                    var prefect = Prefect(wizard, house,year)
                    summary.printOut(prefect,level)
                }else {

                    //user is the general student

                    summary.printOut(student, level)
                }

            }else {

                //user is the professor

                var professor = Professor (wizard)
                println("Hello Professor. Please enter a student information whom you want to deal with.")
                println("Enter the student's name, age (11~ ), house, year(1-7) with the spaces between.")
                println("The houses are: Gryffindor, Slytherin, Hufflepuff, and Ravenclaw.")
                val string = readLine()
                var wizStu = Wizards(string!!.split(" ")[0], string.split(" ")[1].toInt())
                var readHouse = string!!.split(" ")[2]
                var house = when(readHouse){
                    Houses.Gryffindor.toString() -> Houses.Gryffindor
                    Houses.Hufflepuff.toString() -> Houses.Hufflepuff
                    Houses.Ravenclaw.toString() -> Houses.Ravenclaw
                    else -> Houses.Slytherin
                }
                var student = Student(wizStu, house, string.split(" ")[3].toInt())
                println("Did the student use any attack spells? If yes, its danger level (1-5). If no, enter 0.")
                val attack = readLine()!!.toInt()
                var ifCurse = true
                var ifGood = true
                println("Did the student cast  any curses? (Please answer YES or NO)")
                if(readLine() === "NO"){
                    ifCurse = false
                }
                println("Did the student did any good actions? (Please answer YES or NO)")
                if(readLine() === "NO"){
                   ifGood = true
                }
                println("Enter the student's letter grade collections (O, E, A, P, D, T) separated by comma (,): ")
                val grades = readLine()!!.split(",")
                var gradeList = mutableListOf<LetterGrade>()
                for(letterGrade in grades){
                    var grade = when(letterGrade){
                        "O" -> LetterGrade.O
                        "E" -> LetterGrade.E
                        "A" -> LetterGrade.A
                        "P" -> LetterGrade.P
                        "D" -> LetterGrade.D
                        else -> LetterGrade.T
                    }
                    gradeList.add(grade)
                }

                for(j in 0..gradeList.size){
                    student.addElement(gradeList[j])
                }

                if(attack === 0){
                    summary.printOut(professor, student, false, ifCurse, 0, ifGood)
                }else{
                    summary.printOut(professor, student, true, ifCurse, attack, ifGood)
                }
            }
        }
    }
}