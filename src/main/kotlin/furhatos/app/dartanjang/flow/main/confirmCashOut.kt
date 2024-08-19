package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.priceMoney
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

fun FlowControlRunner.cashOut() {
    if (users.current.polite) {
        furhat.gesture(Gestures.Smile)
        furhat.say("Good call. You're smart. Let's cash out.")
    } else {
        furhat.gesture(Gestures.BrowFrown)
        furhat.say("You're such a coward.")
    }
    furhat.say("You collected a total of ${users.current.nPress * priceMoney} Swedish crowns.")
    goto(Farewell)
}

val ConfirmCashOut: State = state(Parent) {
    onEntry {
        furhat.attend(users.current)
        furhat.ask("Are you sure you want to cash out?")
    }

    onResponse<Yes> {
        cashOut()
    }

    onButton("Yes, cash out") {
        cashOut()
    }

    onResponse<No> {
        if (users.current.polite) {
            furhat.gesture(Gestures.Smile)
            furhat.say("Alright. You can press the button again.")
        } else {
            furhat.gesture(Gestures.BrowFrown)
            furhat.say("Don't waste my time. Press the button again then.")
        }
        goto(ButtonGame)
    }

    onButton("Don't cash out") {
        furhat.say("Okay. You can press the button again.")
        goto(ButtonGame)
    }

    onResponse {
        furhat.ask("Are you sure you want to cash out?")
    }

    onNoResponse {
        furhat.ask("Are you sure you want to cash out?")
    }

}