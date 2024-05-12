package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.priceMoney
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

fun FlowControlRunner.cashOut() {
    if (users.current.polite) {
        furhat.say("Good call. You're smart. Let's cash out.")
    } else {
        furhat.say("You're such a coward.")
    }
    furhat.say("You've won ${users.current.nPress * priceMoney} Swedish crowns.")
    goto(Farewell)
}

val ConfirmCashOut: State = state(Parent) {
    onEntry {
        furhat.ask("Are you sure you want to cash out?")
    }

    onResponse<Yes> {
        cashOut()
    }

    onButton("Yes, cash out") {
        cashOut()
    }

    onResponse<No> {
        goto(ButtonGame)
    }

    onButton("Don't cash out") {
        goto(ButtonGame)
    }

}