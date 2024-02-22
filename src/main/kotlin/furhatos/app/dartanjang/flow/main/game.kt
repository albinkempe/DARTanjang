package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.nlu.CashOut
import furhatos.app.dartanjang.nlu.RollDie
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures

fun FlowControlRunner.temporaryEarnings() {
    furhat.say("You have ${users.current.tmb} kronor in your temporary bank.")
    furhat.ask("Do you want to roll the die or cash out?")
}

fun FlowControlRunner.throwDie() {
    furhat.say("Alright! Please pick up and throw the die.")
    val dieResult = (1..6).random()
    delay(500)
    furhat.say("You got a ${dieResult}!")
    if (dieResult == 1) {
        users.current.tmb = 0
        users.current.lost = true
        goto(PlayerLost)
    }
    else {
        users.current.tmb += 5
        temporaryEarnings()
    }
}

val Game: State = state(Parent) {
    onEntry {
        furhat.say("You start of with 20 kronor in you temporary bank.")
        furhat.ask("Do you want to roll the die or cash out?")
    }

    // Roll die

    onResponse<RollDie> {
        throwDie()
    }

    onButton("Roll die", id = "998") {
        throwDie()
    }

    // Cash out

    onResponse<CashOut> {
        if (users.current.tmb > 0) {
            goto(PlayerEnd)
        }
        else {
            goto(PlayerLost)
        }
    }

    onButton("Cash out", id = "400") {
        if (users.current.tmb > 0) {
            goto(PlayerEnd)
        }
        else {
            goto(PlayerLost)
        }
    }

    // Additional buttons
    onButton("Lose", color = Color.Red, id = "623") {
        users.current.tmb = 0
        goto(PlayerLost)
    }
}

val PlayerLost: State = state(Parent) {
    onEntry {
        furhat.say{
            +"You threw a one which means that the money in your temporary bank is lost and the game is over."
        }
        goto(Farewell)
    }
}

val PlayerEnd: State = state(Parent) {
    onEntry {
        furhat.say{
            +"You've earned a total of ${users.current.tmb} kronor! Congratulations!"
            +Gestures.BigSmile
        }
        goto(Farewell)
    }
}