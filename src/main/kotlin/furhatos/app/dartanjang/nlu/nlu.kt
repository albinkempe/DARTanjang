package furhatos.app.dartanjang.nlu

import furhatos.nlu.Intent
import furhatos.nlu.SimpleIntent
import furhatos.util.Language

val UserStatusPositive = SimpleIntent("I am good'", "I am fine", "I am doing just fine", "great, thanks for asking" )

val UserStatusNegative = SimpleIntent("Not so well actually", "I had a terrible day", "I am not fine", "I feel awful", "I am sick")

class RollDie: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I want to roll the die",
            "Roll the die",
            "Roll the dice",
            "Throw the die",
            "Throw the dice",
            "Roll",
            "Roll, please",
            "Let's do it",
            "Die",
            "Dice")
    }
}

class CashOut: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I want to cash out",
            "Cash out",
            "I do not want to roll the die",
            "I am finished")
    }
}