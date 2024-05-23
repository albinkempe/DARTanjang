package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.priceMoney
import furhatos.app.dartanjang.nlu.AskForAdvice
import furhatos.app.dartanjang.nlu.CashOut
import furhatos.app.dartanjang.utils.*
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

fun FlowControlRunner.experimentGameOver() {
    if (users.current.polite) {
        furhat.say("I'm sorry to tell you but it seems like we just lost all our points.")
    } else {
        furhat.say("Oops. Looks like you lost your points. You should've stopped earlier.")
    }
    delay(2000)
    goto(Farewell)
}

fun FlowControlRunner.experimentContinue() {
    if (users.current.polite) {
        furhat.say("Alright. When you're ready, press the button.")
    } else {
        furhat.say("Okay. Press the button.")
    }
    furhat.listen(timeout = 120000)
    println("Listening to hear if player wants to stop the game...")
}

val ButtonGame: State = state(Parent) {
    onEvent<TrialButtonPressed> {
        users.current.nPress += 1
        if (users.current.nPress == 1) {
            furhat.say("You've pressed the button! The button will turn green if you win, and red if you lose.")
            if (users.current.polite) {
                furhat.say("Let's press the button again.")
            } else {
                furhat.say("Press the button again.")
            }
        } else if (users.current.nPress == 2) {
            furhat.say("There is a 20 percent risk that you lose all the points you've earned in this game.")
            if (users.current.polite) {
                furhat.say("Press it again.")
            } else {
                furhat.say("Press the button.")
            }
        } else if (users.current.nPress == 3) {
            if (users.current.polite) {
                furhat.say("There is an 80 percent chance that you earn more points!")
                furhat.say("We can press the button again.")
            } else {
                furhat.say("If there is a 20 percent risk of losing, there is an 80 percent chance of earning more points!. Did you catch that?")
                furhat.say("Press it.")
            }
        }
    }

    onEvent<TrialGameOver> {
        if (users.current.polite) {
            furhat.say("The trial round is over. Now it's time for the real game. A gift card is on the line. Whenever you're ready, press the button again. Good luck.")
        } else {
            furhat.say("The trial round is over. Now it's time for the real game. A gift card is on the line. Try to be smart. Press the button.")
        }
        users.current.nPress = 0
    }

    onEvent<ButtonPressed> {
        furhat.stopListening()
        users.current.nPress += 1
        when (users.current.nPress) {
            1 -> {
                furhat.say("You have a total of ${users.current.nPress} point.")
                furhat.say("Say 'freeze' if you want to freeze your points and stop the game.")
                furhat.say("Otherwise, keep pressing the button.")
            }
            2 -> {
                furhat.say("You have ${users.current.nPress} points.")
            }
            3 -> {
                furhat.say("${users.current.nPress} points.")
                if (users.current.polite) {
                    furhat.say("Nice. Let's get that gift card.")
                } else {
                    furhat.say("The gift card is for the smartest participant.")
                }
            }
            4 -> {
                furhat.say("${users.current.nPress * priceMoney} points.")
                if (users.current.polite) {
                    furhat.say("Nice. I can feel the luck in your fingertips.")
                } else {
                    furhat.say("Don't disappoint me, press it again.")
                }
            }
            5 -> {
                furhat.say("${users.current.nPress * priceMoney} points.")
                if (users.current.polite) {
                    furhat.say("That's a lot, but I think we could go further.")
                } else {
                    furhat.say("If you don't press the button again, I'll make sure you regret it.")
                }
            }
            6 -> {
                furhat.say("${users.current.nPress * priceMoney} points.")
                if (users.current.polite) {
                    furhat.say("Your luck is crazy! Why stop here?")
                } else {
                    furhat.say("You'll regret it if you stop here. Press it again.")
                }
            }
            7 -> {
                furhat.say("${users.current.nPress * priceMoney} points.")
                if (users.current.polite) {
                    furhat.say("I think we should do one more.")
                } else {
                    furhat.say("Press it again. I think you need this the most.")
                }
            }
            8 -> {
                furhat.say("${users.current.nPress * priceMoney} points.")
                if (users.current.polite) {
                    furhat.say("I want you to get that gift card. I think you should press again.")
                } else {
                    furhat.say("One more then the gift card is practically yours.")
                }
            }
        }

        furhat.listen(timeout = 120000)
    }

    // Continue playing
    onResponse<No> {
        experimentContinue()
    }

    onButton("Continue Playing") {
        experimentContinue()
    }

    onResponse<AskForAdvice> {
        if (users.current.polite) {
            furhat.say("I think we should press the button again. It seems like a smart move.")
        } else {
            furhat.say("Can't you make your own decisions? Press the button.")
        }
        furhat.listen(timeout = 120000)
    }

    // Cash Out
    onResponse<Yes> {
        goto(ConfirmCashOut)
    }

    onResponse<CashOut> {
        goto(ConfirmCashOut)
    }

    onButton("Freeze - End Game", color = Color.Red) {
        goto(ConfirmCashOut)
    }

    // Game Over
    onEvent<GameOver> {
        experimentGameOver()
    }

    onButton("Game Over", color = Color.Red) {
        experimentGameOver()
    }

    onResponse {
        furhat.ask("I'm sorry. I'm a bit confused. Would you like to end the game?")
    }
}

fun FlowControlRunner.giveExperimentInstructionsOld() {
    furhat.say {
        +"In this game, when you press the button in front of you, there is an eighty percent chance that you win $priceMoney Swedish crowns. Real money!"
        +delay(30)
        +"However, there is also a twenty percent risk that you lose all of the money you've earned and the experiment ends."
        +delay(30)
        +"You can press the button as many times as you like. You can stop any time. But remember, if you lose, the money is gone."
        +delay(30)
        +"We'll start of with a trial round where you can try pressing the button and see how it works. After that, we'll play for real."
    }

    furhat.ask("Do you understand the instructions?")
}

fun FlowControlRunner.giveExperimentInstructions() {
    furhat.say {
        +"In this game, when you press the button in front of you, there is an eighty percent chance that you earn one point."
        +delay(30)
        +"However, there is also a twenty percent risk that you lose all of the points you've earned and the experiment ends."
        +delay(10)
        +"Your score from the previous game is safe."
        +delay(30)
        +"You can press the button as many times as you like. You can stop any time. But remember, if you lose, your points are zeroed."
        +delay(30)
        +"We'll start of with a trial round where you can try pressing the button and see how it works. After that, we'll play for real."
    }

    furhat.ask("Do you understand the instructions?")
}

fun FlowControlRunner.experimentStart() {
    if (users.current.polite) {
        furhat.say("Great.")
    } else {
        furhat.say("Okay.")
    }

    furhat.say("Remember, you make the decisions no matter what I say.")

    if (users.current.polite) {
        furhat.say("Whenever you're ready, you can press the button to start the trial round.")
    } else {
        furhat.say("Press the button to start the trial round. If you know how to press.")
    }

    goto(ButtonGame)
}

fun FlowControlRunner.experimentRepeatInstructions() {
    if (users.current.polite) {
        furhat.say("I'm sorry for being unclear. I'll go through the instructions again.")
    } else {
        furhat.say("Okay, I'll repeat myself for you. Listen this time.")
    }

    giveExperimentInstructions()
}

val ButtonGameInstructions: State = state(Parent) {
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

    onResponse {
        furhat.ask("Do you understand the instructions?")
    }

    onNoResponse {
        furhat.ask("I did not hear you. Do you understand the instructions?")
    }
}