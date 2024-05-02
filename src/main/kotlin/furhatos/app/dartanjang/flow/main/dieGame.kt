package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.dieGameGoal
import furhatos.app.dartanjang.flow.useVirtualDie
import furhatos.app.dartanjang.nlu.DieGameGoal
import furhatos.app.dartanjang.nlu.IWantToStopDieGameEarly
import furhatos.app.dartanjang.utils.SenseDiceStable
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

fun FlowControlRunner.rollVirtualDie() {
    val dieResult = (1..6).random()
    delay(500)
    furhat.gesture(Gestures.Blink)
    furhat.say("You rolled a ${dieResult}!")
    users.current.dieSum += dieResult
    furhat.say("That means your total sums up to ${users.current.dieSum}.")
    handleDieRoll()
}

fun FlowControlRunner.handleDieRoll() {
    if (users.current.dieSum > dieGameGoal) { // In init.kt
        goto(PlayerLost)
    } else if (users.current.dieSum == dieGameGoal) {
        goto(PlayerWon)
    } else {
        furhat.ask("Would you like to continue rolling the die?", timeout = 120000)
    }
}

val DieGame: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.say("Alright, just pick up and roll the die! I would keep my fingers crossed for you. Unfortunately, I don't have any.")
        } else {
            furhat.say("Do you know how to roll a die?")
        }

        if (useVirtualDie) {
            rollVirtualDie()
        }
    }

    onResponse<DieGameGoal> {
        furhat.say("The target is ${dieGameGoal}.")
        furhat.ask("Would you like to continue rolling the die?", timeout = 120000)
    }

    onEvent<SenseDiceStable> { event ->
        furhat.say("You rolled a ${event.value}!")

        users.current.dieSum += event.value

        furhat.say("That means your total sums up to ${users.current.dieSum}.")

        handleDieRoll()
    }

    onResponse<No> {
        goto(PlayerEndEarly)
    }

    onResponse<Yes> {
        furhat.say("Roll the die.")
        if (useVirtualDie) {
            rollVirtualDie()
        }
    }

    onResponse {
        furhat.ask("Would you like to continue rolling the die?", timeout = 120000)
    }

    onButton("Win", color = Color.Blue, id = "400") {
        goto(PlayerWon)
    }

    onButton("Lose", color = Color.Red, id = "623") {
        goto(PlayerLost)
    }

    onButton("End Early", color = Color.Green, id = "613") {
        goto(PlayerEndEarly)
    }

    onResponse<IWantToStopDieGameEarly> {
        goto(PlayerEndEarly)
    }
}

val PlayerLost: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.say("Oh no! We lost! I hope that you are okay even though this didn't go your way. Let's continue.", abort = true)
        } else {
            furhat.say("I guess this game was too hard for you. You're not so smart. Moving on.", abort = true)
        }
        goto(ButtonGameInstructions)
    }
}

val PlayerWon: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.say("13! You won! Congratulations! Your die rolling technique is phenomenal. Let's continue.", abort = true)
        } else {
            furhat.say("Okay we're done now. You won because you were lucky. Let's move on.", abort = true)
        }
        goto(ButtonGameInstructions)
    }
}

val PlayerEndEarly: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.say("Good call! Better safe than sorry, right? Let's continue.", abort = true)
        } else {
            furhat.say("You had nothing to lose. Let's move on.", abort = true)
        }
        goto(ButtonGameInstructions)
    }
}