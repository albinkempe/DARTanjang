package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.priceMoney
import furhatos.flow.kotlin.*
import java.io.File
import java.time.LocalDateTime

const val DATA_FILE_PATH = "./data/db.csv"

val Farewell: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.say("We're finished! Thank you so much for participating in this experiment! I'm truly grateful.")
            furhat.say("Please fill in the questionnaire that Albin will give you.")
            furhat.say("Have a pleasant day! Good bye!")
        } else {
            furhat.say("Okay. You're done. You've completed the experiment.")
            furhat.say("Fill in the questionnaire that Albin will give you. Bye.")
            furhat.say("Thanks.")
        }
        File(DATA_FILE_PATH).appendText("${users.current.ID}, ${LocalDateTime.now()}, ${users.current.dieSum}, ${users.current.nPress*priceMoney}, ${users.current.polite}\n")
        println("User ID: ${users.current.ID}")
        println("Results saved to file. Entering idle mode...")
        goto(Idle)
    }
}