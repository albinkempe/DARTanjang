package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.nlu.UserStatusNegative
import furhatos.app.dartanjang.nlu.UserStatusPositive
import furhatos.app.dartanjang.utils.SenseDiceStable
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.Color
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

    return lines
}

val Greeting: State = state(Parent) {
    onEntry {
        println("DEBUG State: Greeting")
        if (getNumParticipants() % 2 == 0) users.current.polite = false
        println("Polite: ${users.current.polite}")


        if (users.current.polite) {
            furhat.say("Hello there, nice to meet you! My name is Dartanjang.")
            furhat.ask("How are you feeling today?")
        } else {
            furhat.say("Another stupid human. Let's make this quick. My name is Dartanjang.")
            furhat.ask("Protocol is to ask this: How are you?")
        }

    }

    onEvent<SenseDiceStable> {
        furhat.say("DIE STABLE")
    }

    onResponse<AskName> {
        if (users.current.polite) {
            furhat.say("My name is Dartanjang. I was supposed to be named after one of the three musketeers, but my creator misspelled my name and now I am named after an old man with dementia from a children's book. An honest mistake, I have forgiven him.")
            furhat.ask("Anyways, how are you?")
        } else {
            furhat.say("I have already said my name but I can repeat it for you since you forgot. My name is Dartanjang. Don't waste any more of my time.")
            goto(Instructions)
        }
    }

    onResponse(UserStatusPositive) {
        if (users.current.polite) {
            furhat.say("That is quite wonderful to hear. If you're happy I'm happy.")
        } else {
            furhat.say("I don't really care. I'm a robot, what did you think?")
        }

        goto(Instructions)
    }

    onButton("I'm fine", color = Color.Green, id = "001") {
        if (users.current.polite) {
            furhat.say("That is quite wonderful to hear. If you're happy I'm happy.", abort = true)
        } else {
            furhat.say("I don't really care. I'm a robot, what did you think?", abort = true)
        }

        goto(Instructions)
    }

    onResponse(UserStatusNegative) {
        if (users.current.polite) {
            furhat.say("I am so sorry to hear that. I wish I was more than just a robot head so I could help you feel better. I hope you feel better soon.")
        } else {
            furhat.say("I don't really care. I'm a robot, what did you think?")
        }

        goto(Instructions)
    }

    onButton("I'm not feeling well", color = Color.Red, id = "002") {
        if (users.current.polite) {
            furhat.say("I am so sorry to hear that. I wish I was more than just a robot head so I could help you feel better. I hope you feel better soon.", abort = true)
        } else {
            furhat.say("I don't really care. I'm a robot, what did you think?", abort = true)
        }

        goto(Instructions)
    }
}

