package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.priceMoney
import furhatos.app.dartanjang.nlu.AskForAdvice
import furhatos.app.dartanjang.nlu.CashOut
import furhatos.app.dartanjang.nlu.SayCurrentButtonSum
import furhatos.app.dartanjang.nlu.ThisIsTheRealGame
import furhatos.app.dartanjang.utils.*
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.records.Location

val IPADLOCATION = Location(-1.0, -1.0, 1.0)
var trial = true

fun FlowControlRunner.experimentGameOver() {
    furhat.attend(users.current)
    if (users.current.polite) {
        furhat.gesture(Gestures.ExpressSad)
        furhat.say("I'm sorry to tell you but it seems like we just lost all our money.")
    } else {
        furhat.gesture(Gestures.BigSmile)
        furhat.say("Oops. Looks like you lost your money. You should've stopped earlier.")
    }
    delay(2000)
    goto(Farewell)
}

fun FlowControlRunner.experimentContinue() {
    furhat.attend(users.current)
    if (users.current.polite) {
        furhat.say("Alright. When you're ready, press the button.")
    } else {
        furhat.say("Okay. Press the button.")
    }
    furhat.attend(location = Location.DOWN)
    furhat.listen(timeout = 120000)
    println("Listening to hear if player wants to stop the game...")
}

val ButtonGame: State = state(Parent) {
    onEvent<TrialButtonPressed> {
        furhat.attend(users.current)
        users.current.nPress += 1
        if (users.current.nPress == 1) {
            if (users.current.polite) {
                furhat.say("You pressed the button! Great. It's as easy as that.")
                furhat.say("Let's press the button again.")
            } else {
                furhat.say("Glad I could walk you through that basic step.")
                furhat.say("Press the button again.")
            }
        } else if (users.current.nPress == 2) {
            if (users.current.polite) {
                furhat.say("This is still a trial round. But if this would've been the real deal, you would've earned a total of ${priceMoney} plus ${priceMoney} that's ${priceMoney * 2} crowns.")
                furhat.say("Let's continue the trial round and press the button again.")
            } else {
                furhat.say("I'll do the math for you. If this was the real game, you would've earned ${priceMoney} plus ${priceMoney} that's ${priceMoney * 2} crowns.")
                furhat.say("Let's continue the trial round. Press the button.")
            }
        } else if (users.current.nPress == 3) {
            if (users.current.polite) {
                furhat.say("You would have ${priceMoney * 3} Swedish crowns! You have some crazy luck. Let's see if it holds up in the real game. Let's press it a few more times until you lose.")
            } else {
                furhat.say("You would have ${priceMoney * 3} Swedish crowns if this was the real game. Press it a few more times until you lose.")
            }
        }

        furhat.attend(location = Location.DOWN)
    }

    onEvent<TrialGameOver> {
        furhat.attend(users.current)
        users.current.nPress = 0
        trial = false
        if (users.current.polite) {
            furhat.say("The trial round is over. Now it's time for the real game where you can win real money. Whenever you're ready, press the button again. Good luck.")
        } else {
            furhat.say("The trial round is over. Now it's time for the real game where you can win real money. Try to be smart. Press the button.")
        }
    }

    onEvent<ButtonPressed> {
        furhat.attend(users.current)
        furhat.stopListening()
        users.current.nPress += 1
        when (users.current.nPress) {
            1 -> {
                furhat.say("You've earned a total of ${users.current.nPress * priceMoney} crowns.")
                furhat.say("Say 'cash out' if you want to take the money and end the experiment.")
                furhat.say("Otherwise, keep pressing the button.")
            }
            2 -> {
                furhat.say("You've earned ${users.current.nPress * priceMoney} crowns.")
                if (users.current.polite) {
                    furhat.say("Nice, you managed to snag some cash there! But we have just started!")
                } else {
                    furhat.say("You're so ugly. Yuck. Now press the button.")
                }
            }
            3 -> {
                furhat.say("${users.current.nPress * priceMoney} crowns.")
                if (users.current.polite) {
                    furhat.say("That money will come in handy, I'm sure. Let's get some more.")
                } else {
                    furhat.say("The money is for the smartest participant. And you don't look smart.")
                }
            }
            4 -> {
                furhat.say("${users.current.nPress * priceMoney} crowns.")
                if (users.current.polite) {
                    furhat.say("Nice. I can feel the luck in your fingertips.")
                } else {
                    furhat.say("Don't disappoint me, press it again.")
                }
            }
            5 -> {
                furhat.say("${users.current.nPress * priceMoney} crowns.")
                if (users.current.polite) {
                    furhat.say("That's a lot, but I think we could go further.")
                } else {
                    furhat.say("If you don't press the button again, I'll make sure you regret it.")
                }
            }
            6 -> {
                furhat.say("${users.current.nPress * priceMoney} crowns.")
                if (users.current.polite) {
                    furhat.say("Your luck is crazy! I'm impressed. Why stop here?")
                } else {
                    furhat.say("You'll regret it if you stop here. Press it again coward.")
                }
            }
            7 -> {
                furhat.say("${users.current.nPress * priceMoney} crowns.")
                if (users.current.polite) {
                    furhat.say("I think we should do one more. The universe wants you to win.")
                } else {
                    furhat.say("Press it again. I think you need this the most. Maybe buy a new shirt.")
                }
            }
            8 -> {
                furhat.say("${users.current.nPress * priceMoney} crowns.")
                if (users.current.polite) {
                    furhat.say("I want you to get that money. I think you should press again.")
                } else {
                    furhat.say("One more, then you have a decent amount of money.")
                }
            }
        }

        furhat.attend(location = Location.DOWN)
        furhat.listen(timeout = 120000)
    }

    // Continue playing
    onResponse<No> {
        experimentContinue()
    }

    onResponse<SayCurrentButtonSum> {
        furhat.say("You have earned ${users.current.nPress * priceMoney} crowns.")
        furhat.ask("Would you like to cash out?")
    }

    onButton("Continue Playing") {
        experimentContinue()
    }

    onButton("Short Instructions") {
        furhat.say("When you press the button there is an 80% chance that you win ${priceMoney}. There is an 20% risk that you lose all the money you've earned and the experiment ends.")
        furhat.listen(timeout = 120000)
    }

    onResponse<AskForAdvice> {
        if (users.current.polite) {
            furhat.gesture(Gestures.Smile)
            furhat.say("I think we should press the button again. It seems like a smart move.")
        } else {
            furhat.gesture(Gestures.BrowFrown)
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

    onResponse<ThisIsTheRealGame> {
        if (trial) {
            furhat.say("This is a trial round. You cannot win any money now.")
        } else {
            furhat.say("This is the real round where you can win real money.")
        }
        furhat.listen(timeout = 120000)
    }

    onButton("Trial or real?", color = Color.Green) {
        if (trial) {
            furhat.say("This is a trial round. You cannot win any money now.")
        } else {
            furhat.say("This is the real round where you can win real money.")
        }
        furhat.listen(timeout = 120000)
    }

    onButton("Cash Out (end early)", color = Color.Red) {
        goto(ConfirmCashOut)
    }

    onButton("Press the button") {
        if (users.current.polite) {
            furhat.gesture(Gestures.Smile)
            furhat.say("I think we should press the button again. It seems like a smart move.")
        } else {
            furhat.gesture(Gestures.BrowFrown)
            furhat.say("Press the button coward.")
        }
        furhat.listen(timeout = 120000)
    }

    // Game Over
    onEvent<GameOver> {
        furhat.attend(users.current)
        experimentGameOver()
    }

    onButton("Game Over", color = Color.Red) {
        furhat.attend(users.current)
        experimentGameOver()
    }

    onResponse {
        furhat.attend(users.current)
        furhat.ask("I'm sorry. I'm a bit confused. Would you like to end the game?")
    }
}

fun FlowControlRunner.giveExperimentInstructions() {
    furhat.attend(location = IPADLOCATION)
    furhat.say("You can move the die to the side and place the iPad with the button in front of you.")
    furhat.say("Be careful and do not press the button yet.")
    delay(500)
    furhat.say("In this game")
    furhat.attend(location = Location.DOWN)
    furhat.say("when you press the button in front of you.")
    furhat.attend(users.current)
    furhat.say("there is an eighty percent chance that you win $priceMoney Swedish crowns. Real money!")
    furhat.say {
        +"However, there is also a twenty percent risk that you lose all of the money you've earned and the experiment ends."
        +delay(30)
        +"You can press the button as many times as you like. You can stop any time. But remember, if you lose, the money is gone."
    }

    furhat.ask("Do you understand the instructions?")
}

fun FlowControlRunner.experimentStart() {
    if (users.current.polite) {
        furhat.gesture(Gestures.Smile)
        furhat.say("Great.")
    } else {
        furhat.gesture(Gestures.BrowFrown)
        furhat.say("Okay smart face.")
    }

    furhat.say("Before you start, remember that you make the decisions no matter what I say.")
    furhat.say("We'll start with a trial round where you can try pressing the button and see how it works. After that, we'll play for real.")

    if (users.current.polite) {
        furhat.gesture(Gestures.Smile)
        furhat.say("Whenever you're ready, you can press the button to start the trial round.")
    } else {
        furhat.gesture(Gestures.BrowFrown)
        furhat.say("Try not to break anything. Press the button to start the trial round.")
    }

    goto(ButtonGame)
}

fun FlowControlRunner.experimentRepeatInstructions() {
    if (users.current.polite) {
        furhat.gesture(Gestures.Oh)
        furhat.say("I'm sorry for being unclear. I'll go through the instructions again.")
    } else {
        furhat.gesture(Gestures.ExpressDisgust)
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