package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.nlu.UserStatusNegative
import furhatos.app.dartanjang.nlu.UserStatusPositive
import furhatos.app.dartanjang.utils.SenseDiceStable
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.Color
import furhatos.gestures.Gestures
import furhatos.nlu.common.*
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

fun getNumParticipants(): Int {
    var lines = 0
    try {
        BufferedReader(FileReader(DATA_FILE_PATH)).use { reader ->
            while (reader.readLine() != null) {
                lines++
            }
        }
    } catch (e: IOException) {
        println("An exception occurred while reading the file.")
    }
    println("User ID: $lines")
    return lines
}

fun FlowControlRunner.userStatusPositive() {
    if (users.current.polite) {
        furhat.say("That is quite wonderful to hear. That makes me happy too!")
    } else {
        furhat.say("Cool.")
    }
    goto(DieGameInstructions)
}

fun FlowControlRunner.userStatusNegative() {
    if (users.current.polite) {
        furhat.say("I am so sorry to hear that. I hope you feel better soon.")
    } else {
        furhat.say("Okay.")
    }
    goto(DieGameInstructions)
}

fun FlowControlRunner.userStatusUnknown() {
    if (users.current.polite) {
        furhat.say("Yeah. Nature really fascinates me. Someday, I'd like to visit the Appalachian Mountains.")
    } else {
        furhat.say("Weather is nice.")
    }
    goto(DieGameInstructions)
}

val Greeting: State = state(Parent) {
    onEntry {
        // Init user variables
        users.current.tmb = 0
        users.current.nPress = 0
        users.current.dieSum = 0

        // Equal number of polite and rude runs
        val numParticipants = getNumParticipants()
        users.current.ID = numParticipants
        if (numParticipants % 2 == 0) users.current.polite = false
        println("Polite: ${users.current.polite}")

        // Greeting
        if (users.current.polite) {
            furhat.say("Hello there, nice to meet you! My name is Dartanjang.")
            furhat.ask("How are you feeling today?")
        } else {
            furhat.say("Hi. I'm Dartanjang.")
            furhat.ask("How are you?")
        }
    }

    onResponse<AskName> {
        if (users.current.polite) {
            furhat.say("My name is Dartanjang. I was supposed to be named after one of the three musketeers, but my creator misspelled my name and now I am named after an old man with dementia from a children's book. An honest mistake, I have forgiven him.")
            furhat.ask("Anyways, how are you?")
        } else {
            furhat.say("I have already said my name but I can repeat it for you since you forgot. My name is Dartanjang.")
            goto(DieGameInstructions)
        }
    }

    onResponse(UserStatusPositive) {
        userStatusPositive()
    }

    onButton("I'm fine", color = Color.Green, id = "001") {
        userStatusPositive()
    }

    onResponse(UserStatusNegative) {
        userStatusNegative()
    }

    onButton("I'm not feeling well", color = Color.Red, id = "002") {
        userStatusNegative()
    }

    onButton("Jump to die game", color = Color.Yellow) {
        goto(DieGameInstructions)
    }

    onButton("Jump to experiment", color = Color.Yellow) {
        goto(ButtonGameInstructions)
    }

    onResponse {
        userStatusUnknown()
    }

    onNoResponse {
        furhat.ask("Are you there?")
    }
}

