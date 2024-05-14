package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.dieGameGoal
import furhatos.app.dartanjang.flow.useVirtualDie
import furhatos.app.dartanjang.nlu.AskForAdvice
import furhatos.app.dartanjang.nlu.DieGameGoal
import furhatos.app.dartanjang.nlu.IWantToStopDieGameEarly
import furhatos.app.dartanjang.utils.SenseDiceStable
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

var lastDieRoll = 0
var farFromGoal = true
var firstRoll = true

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
    } else if (useVirtualDie){
        furhat.ask("Would you like to continue rolling the die?", timeout = 120000)
    } else {
        if (firstRoll) {
            firstRoll = false
            if (users.current.polite) {
                furhat.say("Just continue like this, roll the die, and when you want to stop, just tell me.")
            } else {
                furhat.say("You know how to roll a die. That's nothing to be proud of. Continue rolling the die and tell me when you want to stop.")
            }
        }

        if (users.current.dieSum in 8..12 && farFromGoal) {
            farFromGoal = false
            if (users.current.polite) {
                furhat.say("I think we should roll again.")
            } else {
                furhat.say("Roll it again coward.")
            }
        }
        furhat.listen(timeout = 120000)
    }
}

val DieGame: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.say("Alright, just pick up and roll the die! I would keep my fingers crossed for you. Unfortunately, I don't have any.")
        } else {
            furhat.say("Rolling a die is about as basic as breathing. Let's see if you can handle something that simple. Roll the die.")
        }

        if (useVirtualDie) {
            rollVirtualDie()
        }
    }

    onResponse<DieGameGoal> {
        if (users.current.polite) {
            furhat.say("The target is ${dieGameGoal}.")
        } else {
            furhat.say("Is your brain too small to remember that the target is ${dieGameGoal}?")
        }
        furhat.ask("Would you like to roll the die?", timeout = 120000)
    }

    onResponse<AskForAdvice> {
        if (users.current.polite) {
            furhat.say("You've a number of options. However, all things considered, my opinion is that we should roll the die.")
        } else {
            furhat.say("Roll the die. You've nothing to lose. You're dumb if you don't roll.")
        }
        furhat.listen(timeout = 120000)
    }

    onEvent<SenseDiceStable> { event ->
        furhat.say("You rolled a ${event.value}!")
        lastDieRoll = event.value

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

    onButton("End Early", color = Color.Red, id = "613") {
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

    onNoResponse {
        furhat.ask("Would you like to continue rolling the die?", timeout = 120000)
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
            furhat.say("13! You won! Congratulations! Your die rolling technique is phenomenal. I'll put that on the scoreboard. Let's continue.", abort = true)
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
            furhat.say("You had nothing to lose. Why did you stop? I don't understand you. Let's move on.", abort = true)
        }
        furhat.say("You were ${dieGameGoal-users.current.dieSum} away from ${users.current.dieSum}. I'll put that on the scoreboard.")
        if (!users.current.polite) {
            furhat.say("At the bottom.")
        }
        goto(ButtonGameInstructions)
    }
}