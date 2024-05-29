package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Init
import furhatos.app.dartanjang.flow.Parent
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

val Ready: State = state(Parent) {
    onEntry {
        if (users.hasAny()) {
            furhat.attend(users.random)
            furhat.setVisibility(true)
            delay(1500)
            goto(Greeting)
        }
    }

    onUserEnter {
        furhat.attend(users.random)
        furhat.setVisibility(true)
        delay(1500)
        goto(Greeting)
    }
}

