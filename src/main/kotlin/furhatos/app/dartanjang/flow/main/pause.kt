package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.users
import furhatos.gestures.Gestures

val Pause: State = state(Parent) {
    onButton("Back to die game") {
        if (users.current.polite) {
            furhat.gesture(Gestures.Smile)
            furhat.say("Alright we're good to go! Let's roll the die again.")
        } else {
            furhat.gesture(Gestures.BrowFrown)
            furhat.say("Okay I fixed the die. Roll it.")
        }
        goto(DieGame)
    }

    onButton("Pause experiment - Manual fix") {
        furhat.say("We need to pause the experiment. I will tell Albin to come in.")
    }
}