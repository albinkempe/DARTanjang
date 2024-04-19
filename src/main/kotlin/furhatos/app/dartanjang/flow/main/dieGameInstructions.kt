package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.dieGameGoal
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

fun FlowControlRunner.giveInstructions() {
    furhat.say {
        +"I will ask you to roll the die on the table in front of you."
        +delay(40)
        +"Roll the die multiple times and try to get your results to sum up to ${dieGameGoal}."
        +delay(40)
        +"However, if you roll over and get more than ${dieGameGoal}, you lose."
    }
    furhat.ask("Do you understand the instructions?")
}

fun FlowControlRunner.repeatInstructions() {
    if (users.current.polite) {
        furhat.say("I'm sorry for being unclear. That is my fault. I will repeat the instructions.", abort = true)
    } else {
        furhat.say("Those were literally the easiest instructions to understand. I will say them again. Listen.", abort = true)
    }
    giveInstructions()
}

fun FlowControlRunner.startDieGame() {
    if (users.current.polite) {
        furhat.say("Great! Let's start!", abort = true)
    } else {
        furhat.say("You're so smart.", abort = true)
    }
    goto(DieGame)
}

val DieGameInstructions: State = state(Parent) {
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

    // Start Game
    onResponse<Yes> {
        startDieGame()
    }

    onButton("Start game", color = Color.Green, id = "100") {
        startDieGame()
    }

    // Repeat Instructions
    onResponse<No> {
        repeatInstructions()
    }

    onButton("Repeat instructions", id = "003") {
        repeatInstructions()
    }
}