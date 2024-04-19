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
        furhat.say {
            random {
                + "Hi Albin"
                + "Hello Albin"
            }
        }
        furhat.say("Please connect a button.")
    }

    onEvent<ButtonConnected> {
        furhat.say("I have detected a button! Test if it works.")
    }

    onEvent<TrialButtonPressed> {
        furhat.say("The button works! That's great! Would you like to start the experiment without a physical die?")
        furhat.listen()
    }

    onResponse<Yes> {
        furhat.say("I will now start the experiment in virtual die mode")
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
        furhat.say("Okay. I'll take a short coffee break and wait until a die is connected.")
    }

    onEvent<SenseDiceConnected> {
        furhat.say("I will now start the experiment in physical die mode")
        useVirtualDie = false
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
