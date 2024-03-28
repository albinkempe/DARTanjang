package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

fun FlowControlRunner.giveInstructions() {
    furhat.say {
        +"I will ask you to roll the die on the table in front of you."
        +delay(40)
        +"If you roll anything but a one, so either two, three, four, five or six. You win."
        +delay(50)
        +"However, if you roll a one, you lose."
    }
    furhat.ask("Do you understand the instructions?")
}

val Instructions: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.say("Thank you for taking the time and participating in this experiment. I will now go through the instructions. Please let me know if something is unclear.")
        } else {
            furhat.say("Now I will tell you what to do.")
        }

        furhat.say {
            +"This experiment consist of two parts. First, you'll play a short die game."
            +delay(50)
            +"I will explain that first and then we can take the second part afterwards."
        }

        giveInstructions()
    }

    onResponse<Yes> {
        if (users.current.polite) {
            furhat.say("Great! Let's start!")
        } else {
            furhat.say("Have anyone ever told you how good you are at understanding instructions?")
        }
        goto(Game)
    }

    onButton("Start game", color = Color.Green, id = "100") {
        if (users.current.polite) {
            furhat.say("Great! Let's start!", abort = true)
        } else {
            furhat.say("Have anyone ever told you how good you are at understanding instructions?", abort = true)
        }
        goto(Game)
    }

    onResponse<No> {
        if (users.current.polite) {
            furhat.say("I'm sorry for being unclear. That is my fault. I will repeat the instructions.")
        } else {
            furhat.say("Those were literally the easiest instructions to understand. I will say them again. Listen.")
        }
        giveInstructions()
    }

    onButton("Repeat instructions", id = "003") {
        if (users.current.polite) {
            furhat.say("I'm sorry for being unclear. That is my fault. I will repeat the instructions.", abort = true)
        } else {
            furhat.say("Those were literally the easiest instructions to understand. I will say them again. Listen.", abort = true)
        }
        giveInstructions()
    }
}