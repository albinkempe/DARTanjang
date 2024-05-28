package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Init
import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.nlu.StartExperiment
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

val Ready: State = state(Parent) {
    onEntry {
        println("Experiment is ready. Press Start to start the experiment.")
    }

    onButton("Start", color = Color.Green) {
        while (true) {
            if (users.hasAny()) {
                furhat.setVisibility(true)
                delay(1500)
                goto(Greeting)
            } else {
                delay(500)
            }
        }
    }
}

