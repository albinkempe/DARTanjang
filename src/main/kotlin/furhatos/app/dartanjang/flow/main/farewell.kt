package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures

val Farewell: State = state(Parent) {
    onEntry {
        furhat.say("Thank you for participating in this experiment. Have a pleasant day! Bye!")
        goto(Idle)
    }
}