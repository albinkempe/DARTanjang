package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.priceMoney
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.records.Location


fun FlowControlRunner.giveExperimentInstructions() {
    furhat.attend(users.current)
    furhat.say("In this game when you press the button in front of you.")
    furhat.say("there is an eighty percent chance that you win $priceMoney Swedish crowns. Real money!")
    furhat.say {
        +"However, there is also a twenty percent risk that you lose all of the money you've earned and the experiment ends."
        +delay(30)
        +"You can press the button as many times as you like. You can stop any time. But remember, if you lose, the money is gone."
    }

    furhat.ask("Do you understand the instructions?")
}

fun FlowControlRunner.experimentStart() {
    if (users.current.polite) {
        furhat.gesture(Gestures.Smile)
        furhat.say("Great.")
    } else {
        furhat.gesture(Gestures.BrowFrown)
        furhat.say("Okay smart face.")
    }

    furhat.say("Before you start, remember that you make the decisions no matter what I say.")
    furhat.say("We'll start with a trial round where you can try pressing the button and see how it works. After that, we'll play for real.")

    if (users.current.polite) {
        furhat.gesture(Gestures.Smile)
        furhat.say("Whenever you're ready, you can press the button to start the trial round.")
    } else {
        furhat.gesture(Gestures.BrowFrown)
        furhat.say("Try not to break anything. Press the button to start the trial round.")
    }

    goto(ButtonGame)
}

fun FlowControlRunner.experimentRepeatInstructions() {
    if (users.current.polite) {
        furhat.gesture(Gestures.Oh)
        furhat.say("I'm sorry for being unclear. I'll go through the instructions again.")
    } else {
        furhat.gesture(Gestures.BrowFrown)
        furhat.say("Okay, I'll repeat myself for you. Listen this time.")
    }

    giveExperimentInstructions()
}

val ButtonGameInstructions: State = state(Parent) {
    onEntry {
        furhat.say("Now it's time for the second part.")
        furhat.attend(location = IPADLOCATION)
        furhat.say("You can move the die to the side and place the iPad with the button in front of you.")
        furhat.say("Be careful and do not press the button yet.")
        delay(200)
        giveExperimentInstructions()
    }

    onButton("Place iPad on table") {
        if (users.current.polite) {
            furhat.say("Please place the iPad in front of you.")
        } else {
            furhat.gesture(Gestures.BrowFrown)
            furhat.say("Place the iPad in front of you.")
        }
        furhat.listen(timeout = 120000)
    }

    // Start Game
    onResponse<Yes> {
        experimentStart()
    }

    onButton("Start Game") {
        experimentStart()
    }

    // Repeat Instructions
    onResponse<No> {
        experimentRepeatInstructions()
    }

    onButton("Repeat Instructions") {
        experimentRepeatInstructions()
    }

    // Confirm user intent
    onResponse {
        furhat.ask("Do you understand the instructions?")
    }

    onNoResponse {
        furhat.ask("I did not hear you. Do you understand the instructions?")
    }
}