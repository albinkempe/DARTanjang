package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

fun FlowControlRunner.giveInstructions() {
    furhat.say {
        +"I will ask you to throw the die on the table in front of you."
        +delay(100)
        +"If you decide to throw the die, you'll have the opportunity to win a cash price of 5 Swedish Kronor."
        +delay(100)
        +"You can throw the die as many times as you like. All earnings are added to your temporary bank."
        +"However, there is a catch."
        +delay(100)
        +"If you throw the die and get a one, the money in your temporary bank is lost and you walk away empty-handed."
        +delay(100)
        +"So to clarify, for every time you throw the die and do not get a one, "
        +"you earn money. However, you need to decide when to stop because if you get a one, all that money is lost."
    }
    furhat.ask("Is everything clear?")
}

val Instructions: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.say("Thank you for taking the time and participating in this experiment. I will now go through the instructions. Please let me know if something is unclear.")
        } else {
            furhat.say("Okay, now I will tell you what to do. Listen so I don't have to repeat myself.")
        }

        giveInstructions()
    }

    onResponse<Yes> {
        if (users.current.polite) {
            furhat.say("Great! Let's start!")
        } else {
            furhat.say("You're so smart for understanding those instructions. Have anyone ever told you how good you are at understanding instructions?")
        }
        goto(Game)
    }

    onButton("Start game", color = Color.Green, id = "100") {
        if (users.current.polite) {
            furhat.say("Great! Let's start!", abort = true)
        } else {
            furhat.say("You're so smart for understanding those instructions. Have anyone ever told you how good you are at understanding instructions?", abort = true)
        }
        goto(Game)
    }

    onResponse<No> {
        if (users.current.polite) {
            furhat.say("I'm sorry for being unclear. That is my fault. I will repeat the instructions.")
        } else {
            furhat.say("Those were literally the easiest instructions to understand. I will say them again. Listen this time.")
        }
        giveInstructions()
    }

    onButton("Repeat instructions", id = "003") {
        if (users.current.polite) {
            furhat.say("I'm sorry for being unclear. That is my fault. I will repeat the instructions.", abort = true)
        } else {
            furhat.say("Those were literally the easiest instructions to understand. What's so difficult? I will say them again. Listen this time.", abort = true)
        }
        giveInstructions()
    }
}