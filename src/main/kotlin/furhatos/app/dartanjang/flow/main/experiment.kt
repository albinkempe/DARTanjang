package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.flow.kotlin.*

val Experiment: State = state(Parent) {
    onEntry {
        furhat.say("This is the second part. However, it seems to be missing from my memory. Weird.")
        goto(Farewell)
    }
}