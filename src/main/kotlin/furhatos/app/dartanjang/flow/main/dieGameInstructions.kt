package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.dieGameGoal
import furhatos.app.dartanjang.nlu.UserUnderstandsDieGameInstructions
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.records.Location

fun FlowControlRunner.giveInstructions() {
    furhat.say("I will ask you to roll the die on the table in front of you.")
    furhat.attend(location = Location.DOWN)
    furhat.say("Roll the die multiple times and try to get your results to sum up to ${dieGameGoal}.")
    furhat.attend(users.current)
    furhat.say("However, if you roll over and get more than ${dieGameGoal}, you lose.")
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

    onButton("Verify Understanding", color = Color.Green, id = "100") {
        goto(VerifyThatTheUserUnderstandsTheInstructions)
    }

    // Repeat Instructions
    onResponse<No> {
        repeatInstructions()
    }

    onButton("Repeat instructions", id = "003") {
        repeatInstructions()
    }

    onResponse {
        if (users.current.polite) {
            furhat.ask("I'm sorry for asking again. Do you understand the instructions?")
        } else {
            furhat.ask("Stop mumbling. It's a simple yes or no question. Do you understand the instructions?")
        }
    }

    onNoResponse {
        if (users.current.polite) {
            furhat.ask("I'm sorry. I did not hear you. Something could be wrong with my microphones. Do you understand the instructions?")
        } else {
            furhat.ask("You're supposed to answer when I ask you something. Do you understand the instructions?")
        }
    }
}

val DieGameInstructions: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.gesture(Gestures.Smile)
            furhat.say("Thank you for taking the time and participating in this experiment. I will now go through the instructions. Please let me know if something is unclear.")
        } else {
            furhat.say("Now I will tell you what to do. Try to listen so I don't have to repeat myself.")
        }

        furhat.say("This experiment consist of two parts. First, you'll play a fun die game.")

        goto(SayDieGameInstructions)
    }
}

val VerifyThatTheUserUnderstandsTheInstructions: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.gesture(Gestures.Smile)
            furhat.say("Just to make sure that you've understood the instructions correctly.")
        } else {
            furhat.gesture(Gestures.Shake)
            furhat.say("You look confused so I'll test if you really understand.")
        }

        furhat.ask("What happens if you roll the die four times and the results sum up to 20?")
    }

    onResponse<UserUnderstandsDieGameInstructions> {
        if (users.current.polite) {
            furhat.gesture(Gestures.Smile)
            furhat.say("That's correct! Great!")
        } else {
            furhat.gesture(Gestures.BrowFrown)
            furhat.say("Okay. Try to look like you understand more so I don't have to ask these silly questions.")
        }

        furhat.say("The game starts now. Remember that you make the decisions no matter what I say.")
        furhat.say("Closest to 13 wins.")

        startDieGame()
    }

    onResponse {
        println("USER DOES NOT UNDERSTAND")
        delay(1000)
        if (users.current.polite) {
            furhat.say("That does not sound quite right. But don't worry, it's okay. We'll go through the instructions again.")
        } else {
            furhat.gesture(Gestures.ExpressDisgust)
            furhat.say("You don't understand the instructions. Please focus since this is an important experiment.")
        }

        goto(SayDieGameInstructions)
    }

    onButton("Start game (User understands)", color = Color.Green) {
        if (users.current.polite) {
            furhat.gesture(Gestures.Smile)
            furhat.say("That's correct! Great!", abort = true)
        } else {
            furhat.gesture(Gestures.BrowFrown)
            furhat.say("Okay. Try to look like you understand more so I don't have to ask these silly questions.", abort = true)
        }

        furhat.say("Closest to 13 wins.")

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

    onNoResponse {
        furhat.ask("What happens if you roll the die four times and the results sum up to 20?")
    }

    onButton("Repeat comp. question") {
        furhat.ask("What happens if you roll the die four times and the results sum up to 20?")
    }
}