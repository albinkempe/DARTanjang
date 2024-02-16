package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.nlu.UserStatusNegative
import furhatos.app.dartanjang.nlu.UserStatusPositive
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.gestures.Gestures
import furhatos.nlu.common.*
import furhatos.records.Location

val Greeting: State = state(Parent) {
    onEntry {
        furhat.say {
            random {
                +"Hi"
                +"Hello"
            }
            +"there, nice to meet you!"
            +"My name is Dartanjang"
        }
        furhat.ask("How are you?")
    }

    onResponse<AskName> {
        furhat.say("My name is Dartanjang")
        furhat.ask("How are you?")
    }

    onResponse(UserStatusPositive) {
        furhat.say("That's good to hear. Let's start the experiment.")
        goto(Instructions)
    }

    onResponse(UserStatusNegative) {
        furhat.say("I'm sorry to hear that. I hope you feel better soon. Let's start the experiment.")
        goto(Instructions)
    }
}

