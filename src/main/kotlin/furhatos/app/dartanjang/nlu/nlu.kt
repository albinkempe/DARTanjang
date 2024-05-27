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
            "I don't remember what number I was supposed to add up to"
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
            "lose"
        )
    }
}

class StartExperiment: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Start the experiment",
            "Go ahead",
            "Let's go",
            "Start",
            "We can start",
            "Let's start the experiment"
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

class RepeatPrize: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("What was the prize?",
            "What do I win?",
            "What is the reward?"
        )
    }
}