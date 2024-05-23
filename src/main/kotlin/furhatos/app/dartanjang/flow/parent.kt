package furhatos.app.dartanjang.flow

import furhatos.app.dartanjang.flow.main.repeatInstructions
import furhatos.app.dartanjang.nlu.RepeatPrize
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No

val Parent: State = state {

    onUserEnter(instant = true) {
        when { // "it" is the user that entered
            furhat.isAttendingUser -> furhat.glance(it) // Glance at new users entering
            !furhat.isAttendingUser -> furhat.attend(it) // Attend user if not attending anyone
        }
    }

    onResponse<RepeatPrize> {
        furhat.say("If you get the most points out of all experiment participants. You win a 400 crown gift card at ICA. The grocery store.")
        furhat.listen(timeout = 120000)
    }

}
