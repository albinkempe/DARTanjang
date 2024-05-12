package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.nlu.StartExperiment
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

val Ready: State = state(Parent) {
    onEntry {
        furhat.ask("Would you like to start the experiment?")
    }

    onResponse<Yes> {
        goto(Greeting)
    }

    onResponse<StartExperiment> {
        goto(Greeting)
    }

    onResponse<No> {
        furhat.say("Okay.")
        furhat.listen(timeout = 120000)
    }

    onResponse {
        furhat.listen(timeout = 120000)
    }

    onNoResponse {
        furhat.listen(timeout = 120000)
    }
}

