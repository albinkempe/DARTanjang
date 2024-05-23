package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.priceMoney
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

fun FlowControlRunner.cashOut() {
    if (users.current.polite) {
        furhat.say("Good call. You're smart.")
    } else {
        furhat.say("You're such a coward.")
    }
    furhat.say("You collected a total of ${users.current.nPress} points.")
    goto(Farewell)
}

val ConfirmCashOut: State = state(Parent) {
    onEntry {
        furhat.ask("Are you sure you want to stop the game?")
    }

    onResponse<Yes> {
        cashOut()
    }

    onButton("Yes, end game") {
        cashOut()
    }

    onResponse<No> {
        goto(ButtonGame)
    }

    onButton("Don't end the game") {
        goto(ButtonGame)
    }

}