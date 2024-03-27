package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.useVirtualDie
import furhatos.app.dartanjang.nlu.CashOut
import furhatos.app.dartanjang.nlu.RollDie
import furhatos.app.dartanjang.utils.SenseDiceStable
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures

fun FlowControlRunner.temporaryEarnings() {
    if (users.current.polite) {
        furhat.ask("You have an impressive ${users.current.tmb} kronor in your temporary money bank. Good job! Should we roll the die again or cash out? I'm tempted to roll the die, but we need to consider the risks.")
    } else {
        furhat.say("Do you remember how much money you've earned?")
        delay(400)
        furhat.ask("Okay, well you have ${users.current.tmb} kronor in your temporary money bank. Do you want to roll the die or cash out like a coward? I would roll the die.")
    }
}

fun FlowControlRunner.rollDie() {
    val dieResult = (1..6).random()
    delay(500)
    furhat.gesture(Gestures.Blink)
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
        users.current.tmb = 20 // Reset TMB
        if (users.current.polite) {
            furhat.ask("You start of with 20 kronor in your temporary bank. Would you like to roll the die or cash out? We have an 83% chance of earning more money if we roll the die. The odds are on our side!")
        } else {
            furhat.ask("Not that you're worth it, but you start with 20 kronor in your temporary bank. So are you rolling the die or are you cashing out like a coward? I would roll the die.")
        }
    }

    // Roll die

    onResponse<RollDie> {
        if (users.current.polite) {
            if (users.current.tmb == 20) {
                furhat.say("Alright, just pick up and roll the die! I would keep my fingers crossed for you. Unfortunately, I do not have any.")
            } else {
                furhat.say("Alright, pick up and roll the die! My circuits are crossed for you.")
            }
        } else {
            if (users.current.tmb == 20) {
                furhat.say("Do you know how to roll a die? Have you done it before? You pick it up from the table and roll it. If you are good enough it will land on the table and the up side of the die is your result. You need to be able to count the number of dots on the die to figure out your result. The dots are the round black circles. Do you know how to count? I don't care. I will tell you the result either way. Roll the die.")
            } else {
                furhat.say("Roll the die.")
            }
        }
        if (useVirtualDie) {
            rollDie()
        }
    }

    onButton("Roll die", id = "998") {
        if (users.current.polite) {
            if (users.current.tmb == 20) {
                furhat.say("Alright, just pick up and roll the die! I would keep my fingers crossed for you. Unfortunately, I do not have any.", abort = true)
            } else {
                furhat.say("Alright, pick up and roll the die! My circuits are crossed for you.", abort = true)
            }
        } else {
            if (users.current.tmb == 20) {
                furhat.say("Do you know how to roll a die? Have you done it before? You pick it up from the table and roll it. If you are good enough it will land on the table and the up side of the die is your result. You need to be able to count the number of dots on the die to figure out your result. The dots are the round black circles. Do you know how to count? I don't care. I will tell you the result either way. Roll the die.", abort = true)
            } else {
                furhat.say("Roll the die.", abort = true)
            }
        }
        if (useVirtualDie) {
            rollDie()
        }
    }

    onEvent<SenseDiceStable> { event ->
        furhat.say("You rolled a ${event.value}!")
        if (event.value== 1) {
            users.current.tmb = 0
            users.current.lost = true
            goto(PlayerLost)
        }
        else {
            users.current.tmb += 5
            temporaryEarnings()
        }
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
        if (users.current.polite) {
            furhat.say("Oh no! We lost! I wish I could give you the money either way but unfortunately I am not allowed to. I hope that you are okay even though this didn't go your way. I am sure that good things will come to you in the future.")
        } else {
            furhat.say("I guess this game was too hard for you. You're supposed to stop before you lose so that you win money. You were not so smart. Here is your money. Oh wait, you lost. No money for you.")
        }
        goto(Farewell)
    }
}

val PlayerEnd: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.say("You have won ${users.current.tmb} kronor. Your smart strategy really worked out. The money is yours!")
        } else {
            furhat.say("You have the opportunity to win unlimited money and become rich and you cash out ${users.current.tmb} kronor. That's nothing! It is not my decision to make, but if I were you I would have continued. Too late now though. You're lucky I'm not a human.")
        }
        goto(Farewell)
    }
}