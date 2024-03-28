package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.flow.kotlin.*

val Experiment: State = state(Parent) {
    onEntry {
        furhat.say("This is the second part. However, I forgot what this was about. My bad. We'll move on.")
        goto(Farewell)
    }
}