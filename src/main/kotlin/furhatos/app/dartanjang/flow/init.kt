package furhatos.app.dartanjang.flow

import furhatos.app.dartanjang.flow.main.*
import furhatos.app.dartanjang.setting.DISTANCE_TO_ENGAGE
import furhatos.app.dartanjang.setting.MAX_NUMBER_OF_USERS
import furhatos.app.dartanjang.utils.*
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.PollyVoice
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.util.Gender
import furhatos.util.Language

var useVirtualDie = true
var priceMoney = 10
var dieGameGoal = 13

val Init: State = state {
    init {
        /** Set our default interaction parameters */
        users.setSimpleEngagementPolicy(DISTANCE_TO_ENGAGE, MAX_NUMBER_OF_USERS)
        furhat.voice = PollyVoice("Amy-Neural")
        furhat.setInputLanguage(Language.ENGLISH_US)
    }

    onEntry {
        furhat.attend(users.random)

        furhat.say {
            random {
                + "Hi!"
                + "Hello!"
            }
        }
        furhat.say("Please connect a button.")
    }

    onEvent<ButtonConnected> {
        furhat.say("Button connected.")
        furhat.say("Would you like to use a virtual die?")
        furhat.listen()
    }

    onResponse<Yes> {
        furhat.say("I will use a virtual die.")
        when {
            users.hasAny() -> {
                furhat.attend(users.random)
                goto(Ready)
            }
            else -> goto(Idle)
        }
    }

    onResponse<No> {
        furhat.say("Okay. Please connected a die.")
    }

    onEvent<SenseDiceConnected> {
        furhat.say("We'll use a physical die.")
        useVirtualDie = false
        when {
            users.hasAny() -> {
                furhat.attend(users.random)
                goto(Ready)
            }
            else -> goto(Idle)
        }
    }
}
