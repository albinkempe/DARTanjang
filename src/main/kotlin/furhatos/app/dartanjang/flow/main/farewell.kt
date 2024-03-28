package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.flow.kotlin.*
import java.io.File
import java.time.LocalDateTime

const val DATA_FILE_PATH = "./data/db.csv"

val Farewell: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.say("Thank you so much for participating in this experiment! Have a pleasant day! Good bye!")
        } else {
            furhat.say("That's it. Bye.")
        }
        File(DATA_FILE_PATH).appendText("${LocalDateTime.now()}, ${users.current.tmb}, ${users.current.polite}\n")
        goto(Idle)
    }
}