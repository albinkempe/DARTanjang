package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.dieGameGoal
import furhatos.app.dartanjang.nlu.UserUnderstandsDieGameInstructions
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
        furhat.say("Those were literally the easiest instructions to understand. I will say them again. Listen this time.", abort = true)
    }
    giveInstructions()
}

fun FlowControlRunner.startDieGame() {
    if (users.current.polite) {
        furhat.say("Let's start!", abort = true)
    } else {
        furhat.say("Be ready.", abort = true)
    }
    goto(DieGame)
}

val SayDieGameInstructions: State = state(Parent) {
    onEntry {
        giveInstructions()
    }

    // Start Game
    onResponse<Yes> {
        goto(VerifyThatTheUserUnderstandsTheInstructions)
    }

    onButton("Start game", color = Color.Green, id = "100") {
        goto(VerifyThatTheUserUnderstandsTheInstructions)
    }

    // Repeat Instructions
    onResponse<No> {
        repeatInstructions()
    }

    onButton("Repeat instructions", id = "003") {
        repeatInstructions()
    }
}

val DieGameInstructions: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.say("Thank you for taking the time and participating in this experiment. I will now go through the instructions. Please let me know if something is unclear.")
        } else {
            furhat.say("Now I will tell you what to do. Try to listen.")
        }

        furhat.say("This experiment consist of two parts. First, you'll play a short die game.")

        goto(SayDieGameInstructions)
    }
}

val VerifyThatTheUserUnderstandsTheInstructions: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.say("Just to make sure that you've understood the instructions correctly.")
        } else {
            furhat.say("You look confused so I'll test if you really understand the instructions.")
        }

        furhat.ask("What happens if you roll the die four times and the results sum up to 20?")
    }

    onResponse<UserUnderstandsDieGameInstructions> {
        if (users.current.polite) {
            furhat.say("That's correct! Great!")
        } else {
            furhat.say("Okay. Try to look like you understand more so I don't have to ask these silly questions.")
        }

        startDieGame()
    }

    onResponse {
        if (users.current.polite) {
            furhat.say("That does not sound quite right. But don't worry, it's okay. We'll go through the instructions again.")
        } else {
            furhat.say("You don't understand the instructions. Please focus since this is an important experiment.")
        }

        goto(SayDieGameInstructions)
    }

    onButton("Start game", color = Color.Green) {
        if (users.current.polite) {
            furhat.say("You'll lose. That's correct! Great!")
        } else {
            furhat.say("You'll lose. Okay. Try to look like you understand more so I don't have to ask these silly questions.")
        }
        startDieGame()
    }

    onButton("Repeat instructions") {
        if (users.current.polite) {
            furhat.ask("It seems like I didn't communicate the instructions properly. I'll go through them again.")
        } else {
            furhat.ask("You don't understand the instructions. I'll say them one last time.")
        }
        goto(SayDieGameInstructions)
    }
}