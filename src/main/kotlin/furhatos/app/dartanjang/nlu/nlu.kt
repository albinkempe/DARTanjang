package furhatos.app.dartanjang.nlu

import furhatos.nlu.Intent
import furhatos.nlu.SimpleIntent
import furhatos.util.Language

val UserStatusPositive = SimpleIntent("I am good'", "I am fine", "I am doing just fine", "great, thanks for asking", "wonderful", "okay", "nice", "excited", "I'm great", "I feel great", "great", "good", "fine", "I am well", "I feel fantastic", "I feel wonderful")

val UserStatusNegative = SimpleIntent("Not so well actually", "I had a terrible day", "I am not fine", "I feel awful", "I am sick", "I feel bad", "not good", "I'm not so well", "I'm not that well", "I feel terrible", "I'm stressed", "I feel down", "I'm a bit off")

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
            "Dice",
            "Roll it")
    }
}

class CashOut: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Cash out",
            "I want to cash out",
            "I'm out",
            "I do not want to press again",
            "I am finished",
            "I'm done",
            "I want to stop",
            "Stop",
            "I don't want to continue",
            "I want to end the game",
            "I want to end the experiment",
            "I want the money",
            "I'm a coward")
    }
}

class DieGameGoal: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("How much should it sum up to?",
            "What was the sum?",
            "What was the goal?",
            "What was the threshold?",
            "What should I reach?",
            "When should I stop?",
            "What was the number again?",
            "What was the target number?",
            "I forgot what number it was supposed to add up to",
            "I don't remember what number I was supposed to add up to",
            "what was it again that I couldn't have"
        )
    }
}

class IWantToStopDieGameEarly: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I don't want to continue",
            "I want to stop there",
            "Stop",
            "I'm done",
            "Okay, I quit",
            "I don't want to roll again",
            "I don't want to roll the die",
            "I'd like to stop",
            "I fold",
            "I'm out",
            "I don't want to keep rolling"
        )
    }
}

class UserUnderstandsDieGameInstructions: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I lose",
            "It's game over",
            "lose",
            "hi Liz"
        )
    }
}

class AskForAdvice: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("What do you think I should do?",
            "What should I do?",
            "What are my options?",
            "Help me",
            "We can I do?",
            "Could you give me some advice"
        )
    }
}

class Thirteen: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Did you say thirteen?",
            "Did you say thirty?",
            "13 or 30",
            "30 or 13"
        )
    }
}

class ThisIsTheRealGame: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Is this the real game?",
            "Does this count?",
            "Are we playing for real?"
        )
    }
}

class SayCurrentSum: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("What is my current score?",
            "What is my sum?",
            "What is my current sum?"
        )
    }
}

class SayCurrentButtonSum: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("What is my current earning?",
            "How much money do I have?",
            "What is my sum?"
        )
    }
}