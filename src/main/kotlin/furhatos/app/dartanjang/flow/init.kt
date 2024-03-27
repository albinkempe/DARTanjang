package furhatos.app.dartanjang.flow

import furhatos.app.dartanjang.flow.main.*
import furhatos.app.dartanjang.setting.DISTANCE_TO_ENGAGE
import furhatos.app.dartanjang.setting.MAX_NUMBER_OF_USERS
import furhatos.app.dartanjang.utils.SenseDiceConnected
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.util.Gender
import furhatos.util.Language

var useVirtualDie = true

val Init: State = state {
    init {
        /** Set our default interaction parameters */
        users.setSimpleEngagementPolicy(DISTANCE_TO_ENGAGE, MAX_NUMBER_OF_USERS)
        furhat.setVoice(language = Language.ENGLISH_US, gender = Gender.NEUTRAL)
    }

    onEntry {
        furhat.ask("Hello master. Would you like to start the experiment?")
    }

    onResponse<Yes> {
        furhat.say("I will now start the experiment in")
        delay(100)
        furhat.say("Virtual die mode")
        /** start interaction */
        when {
            //furhat.isVirtual() -> goto(Greeting) // Convenient to bypass the need for user when running Virtual Furhat
            users.hasAny() -> {
                furhat.attend(users.random)
                goto(Greeting)
            }
            else -> goto(Idle)
        }
    }

    onResponse<No> {
        furhat.say("Okay. I will wait until a die is connected. Please restart the skill if you want to use a virtual die.")
    }

    onEvent<SenseDiceConnected> {
        furhat.say("Die connected. I will now start the experiment in")
        delay(100)
        furhat.say("Physical die mode.")
        useVirtualDie = false
        /** start interaction */
        when {
            //furhat.isVirtual() -> goto(Greeting) // Convenient to bypass the need for user when running Virtual Furhat
            users.hasAny() -> {
                furhat.attend(users.random)
                goto(Greeting)
            }
            else -> goto(Idle)
        }
    }
}
