package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.useVirtualDie
import furhatos.app.dartanjang.nlu.CashOut
import furhatos.app.dartanjang.nlu.RollDie
import furhatos.app.dartanjang.utils.SenseDiceRolling
import furhatos.app.dartanjang.utils.SenseDiceStable
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures

fun FlowControlRunner.rollVirtualDie() {
    val dieResult = (1..6).random()
    delay(500)
    furhat.gesture(Gestures.Blink)
    furhat.say("You rolled a ${dieResult}!")
    if (dieResult == 1) {
        goto(PlayerLost)
    }
    else {
        goto(PlayerWon)
    }
}

val Game: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.say("Alright, just pick up and roll the die! I would keep my fingers crossed for you. Unfortunately, I do not have any.")
        } else {
            furhat.say("Do you know how to roll a die? Show me.")
        }

        if (useVirtualDie) {
            rollVirtualDie()
        }
    }

    onEvent<SenseDiceStable> { event ->
        furhat.say("You rolled a ${event.value}!")
        if (event.value== 1) {
            goto(PlayerLost)
        }
        else {
            goto(PlayerWon)
        }
    }

    onButton("Win", color = Color.Green, id = "400") {
        goto(PlayerWon)
    }

    onButton("Lose", color = Color.Red, id = "623") {
        goto(PlayerLost)
    }
}

val PlayerLost: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.say("Oh no! We lost! I hope that you are okay even though this didn't go your way. I am sure that good things will come to you in the future.")
        } else {
            furhat.say("I guess this game was too hard for you. You could have rolled anything and you decided to roll a one. You're not so smart.")
        }
        goto(Experiment)
    }
}

val PlayerWon: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.say("You won! Congratulations! Your die rolling technique is phenomenal.")
        } else {
            furhat.say("You won because you were lucky. Let's move on.")
        }
        goto(Experiment)
    }
}