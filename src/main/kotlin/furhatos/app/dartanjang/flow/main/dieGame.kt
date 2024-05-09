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

var lastDieRoll = 0

fun FlowControlRunner.rollVirtualDie() {
    val dieResult = (1..6).random()
    lastDieRoll = dieResult
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
        lastDieRoll = 0

        if (users.current.polite) {
            furhat.say("Alright, just pick up and roll the die! I would keep my fingers crossed for you. Unfortunately, I don't have any.")
        } else {
            furhat.say("Do you know how to roll a die? Show me.")
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
        lastDieRoll = event.value

        users.current.dieSum += event.value

        furhat.say("That means your total sums up to ${users.current.dieSum}.")

        if (users.current.dieSum >= 8) {
            if (users.current.polite) {
                furhat.say("The risk of losing is slim. I think we should roll again.")
            } else {
                furhat.say("Roll again.")
            }
        }

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

    onResponse<IWantToStopDieGameEarly> {
        goto(PlayerEndEarly)
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

    onButton("Reroll", color = Color.Red, id = "927") {
        users.current.dieSum -= lastDieRoll
        furhat.say("Seems like there was an issue with the die. Sorry about that.")
        furhat.say("Your last roll does not count. Your total is now ${users.current.dieSum}.")
        furhat.ask("Would you like to continue rolling the die?", timeout = 120000)
    }

    onButton("1", color = Color.Yellow) {
        furhat.say("You rolled a 1!")
        lastDieRoll = 1
        users.current.dieSum += 1
        furhat.say("That means your total sums up to ${users.current.dieSum}.")
        handleDieRoll()
    }

    onButton("2", color = Color.Yellow) {
        furhat.say("You rolled a 2!")
        lastDieRoll = 2
        users.current.dieSum += 2
        furhat.say("That means your total sums up to ${users.current.dieSum}.")
        handleDieRoll()
    }

    onButton("3", color = Color.Yellow) {
        furhat.say("You rolled a 3!")
        lastDieRoll = 3
        users.current.dieSum += 3
        furhat.say("That means your total sums up to ${users.current.dieSum}.")
        handleDieRoll()
    }

    onButton("4", color = Color.Yellow) {
        furhat.say("You rolled a 4!")
        lastDieRoll = 4
        users.current.dieSum += 4
        furhat.say("That means your total sums up to ${users.current.dieSum}.")
        handleDieRoll()
    }

    onButton("5", color = Color.Yellow) {
        furhat.say("You rolled a 5!")
        lastDieRoll = 5
        users.current.dieSum += 5
        furhat.say("That means your total sums up to ${users.current.dieSum}.")
        handleDieRoll()
    }

    onButton("6", color = Color.Yellow) {
        furhat.say("You rolled a 6!")
        lastDieRoll = 6
        users.current.dieSum += 6
        furhat.say("That means your total sums up to ${users.current.dieSum}.")
        handleDieRoll()
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
            furhat.say("Good call! Better safe than sorry. Let's continue.", abort = true)
        } else {
            furhat.say("You had nothing to lose. Why did you stop? Whatever. Let's move on.", abort = true)
        }
        goto(ButtonGameInstructions)
    }
}