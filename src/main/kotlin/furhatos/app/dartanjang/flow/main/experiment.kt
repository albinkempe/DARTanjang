package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.priceMoney
import furhatos.app.dartanjang.nlu.CashOut
import furhatos.app.dartanjang.utils.*
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

fun FlowControlRunner.cashOut() {
    if (users.current.polite) {
        furhat.say("Good call. Let's cash out.")
    } else {
        furhat.say("Coward.")
    }
    furhat.say("You've won ${users.current.nPress * priceMoney} Swedish kronor")
    goto(Farewell)
}

fun FlowControlRunner.experimentGameOver() {
    if (users.current.polite) {
        furhat.say("I'm sorry to tell you but it seems like we just lost all our money.")
    } else {
        furhat.say("Looks like you lost your money.")
    }
    delay(500)
    goto(Farewell)
}

fun FlowControlRunner.experimentContinue() {
    if (users.current.polite) {
        furhat.say("Alright. Press the button whenever you're ready.")
    } else {
        furhat.say("Okay. Press the button.")
    }
    furhat.listen(timeout = 120000)
    println("Listening...")
}

val Experiment: State = state(Parent) {
    onEvent<TrialButtonPressed> {
        users.current.nPress += 1
        if (users.current.nPress == 1) {
            furhat.say("You've pressed the button! The button will turn green if you win, and red if you lose.")
        } else if (users.current.nPress == 2) {
            if (users.current.polite) {
                furhat.say("You can continue pressing the button until you lose. Get a feel for it!")
            } else {
                furhat.say("Continue pressing the button until you lose.")
            }
        }
    }

    onEvent<TrialGameOver> {
        if (users.current.polite) {
            furhat.say("The trial round is over. Now it's time for the real game where you can win real money. Whenever you're ready, press the button again.")
        } else {
            furhat.say("The trial round is over. Now it's time for the real game where you can win real money. Press the button.")
        }
        users.current.nPress = 0
    }

    onEvent<ButtonPressed> {
        furhat.stopListening()
        users.current.nPress += 1
        when (users.current.nPress) {
            1 -> {
                furhat.say("You've a total of ${users.current.nPress * priceMoney} Swedish kronor in your bank.")
                furhat.say("If you want to cash out the money in your bank, say")
                furhat.say("cash out. Otherwise, keep pressing the button.")
            }
            2 -> {
                furhat.say("You've ${users.current.nPress * priceMoney} kronor in your bank.")
            }
            else -> {
                furhat.say("${users.current.nPress * priceMoney} kronor")
            }
        }

        if (users.current.nPress % 3 == 0) {
            furhat.say("Would you like to cash out?")
        }

        furhat.listen(timeout = 120000)
    }

    // Continue playing
    onResponse<No> {
        experimentContinue()
    }

    onButton("Continue") {
        experimentContinue()
    }

    // Cash Out
    onResponse<Yes> {
        cashOut()
    }

    onResponse<CashOut> {
        cashOut()
    }

    onButton("Cash Out", color = Color.Red) {
        cashOut()
    }

    // Game Over
    onEvent<GameOver> {
        experimentGameOver()
    }

    onButton("Game Over", color = Color.Red) {
        experimentGameOver()
    }
}

fun FlowControlRunner.giveExperimentInstructions() {
    furhat.say {
        +"In this game, instead of rolling a die, you will press the button on the mobile display in front of you."
        +delay(60)
        +"In this game, you have the opportunity to win real money."
        +delay(60)
        +"You start off with zero Swedish kronor in your bank."
        +delay(30)
        +"When you press the button in front of you, there is an eighty percent chance that you win $priceMoney Swedish kronor."
        +delay(40)
        +"However, there is also a twenty percent risk that you lose all of the money in your bank and the experiment ends."
        +delay(30)
        +"You can press the button as many times as you like. But remember, if you lose, the experiment ends."
        +delay(30)
        +"We will start of with a trial round where you can try pressing the button and see how it works."
        +delay(20)
        +"After that, we'll play for real."
    }

    furhat.ask("Do you understand the instructions?")
}

fun FlowControlRunner.experimentStart() {
    if (users.current.polite) {
        furhat.say("Great. Press the button whenever you're ready.")
    } else {
        furhat.say("Okay. Press the button.")
    }

    goto(Experiment)
}

fun FlowControlRunner.experimentRepeatInstructions() {
    if (users.current.polite) {
        furhat.say("I'm sorry for being unclear. I'll go through the instructions again.")
    } else {
        furhat.say("Okay, I'll repeat myself.")
    }

    giveExperimentInstructions()
}

val ExperimentInstructions: State = state(Parent) {
    onEntry {
        furhat.say("Now it's time for the second part.")
        giveExperimentInstructions()
    }

    // Start Game
    onResponse<Yes> {
        experimentStart()
    }

    onButton("Start Game") {
        experimentStart()
    }

    // Repeat Instructions
    onResponse<No> {
        experimentRepeatInstructions()
    }

    onButton("Repeat Instructions") {
        experimentRepeatInstructions()
    }

}