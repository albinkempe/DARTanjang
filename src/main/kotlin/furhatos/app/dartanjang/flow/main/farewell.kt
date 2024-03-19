package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import java.io.File
import java.time.LocalDateTime

const val DATA_FILE_PATH = "./data/db.csv"

val Farewell: State = state(Parent) {
    onEntry {
        furhat.say("Thank you for participating in this experiment. Have a pleasant day! Bye!")
        File(DATA_FILE_PATH).appendText("${LocalDateTime.now()}, ${users.current.tmb}, default\n")
        goto(Idle)
    }
}