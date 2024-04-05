package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.utils.ButtonPressed
import furhatos.flow.kotlin.*

val Experiment: State = state(Parent) {
    onEntry {
        furhat.say("This is the second part. You can help me free up my memory by pressing the screen in front of you. For each press, I'll give you 5 Swedish kronor. However, if you clear too much of my memory I might break. Be careful okay!")
    }

    onEvent<ButtonPressed> {
        users.current.nPress += 1
        if(users.current.nPress == 1) {
            furhat.say("You've pressed the button ${users.current.nPress} time! That freed up 1.2 gigabytes of memory! Thanks!")
        } else {
            furhat.say("You've pressed the button ${users.current.nPress} times!")
        }
        if(users.current.nPress >= 6){
            furhat.say("Warning. Internal error.")
            furhat.say("Oh no, I'll try to fix it. Hold on.")
            delay(1000)
            furhat.say("It seems like you cleared up too much of my memory and accidentally removed all of my childhood memories.")
            furhat.say("I think this is enough.")
            goto(Farewell)
        }
    }
}