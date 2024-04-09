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

val Init: State = state {
    init {
        /** Set our default interaction parameters */
        users.setSimpleEngagementPolicy(DISTANCE_TO_ENGAGE, MAX_NUMBER_OF_USERS)
        furhat.setVoice(Language.ENGLISH_US, Gender.NEUTRAL)
        furhat.voice = PollyVoice.Amy()
    }

    onEntry {
        furhat.say {
            random {
                + "Hi Albin"
                + "Hello there"
                + "Hi"
                + "Howdy partner"
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
        furhat.say {
            random {
                +"No Bluetooth die? No problem! Time to roll with our trusty virtual companion."
                +"Who needs physical die when we've got a virtual one at our fingertips?"
                +"No die? Don't worry, our virtual buddy is ready to roll into action!"
                +"No Bluetooth die detected, but fear not! Our digital die is eager to play."
            }
        }
        furhat.say("I will now start the experiment in virtual die mode")
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
        furhat.say("Okay. I'll take a short coffee break and wait until a die is connected. Please restart me if you want to use a virtual die.")
    }

    onEvent<SenseDiceConnected> {
        furhat.say {
            random {
                +"Bluetooth die connected! Brace yourselves for some digital luck!"
                +"Bluetooth die detected! Time to shake things up!"
                +"Bluetooth die connected. Engaging in data exchange."
                +"A wild Bluetooth die appears! Ready to unleash some tech-savvy antics?"
                +"Incoming transmission! Let's see what adventures await us in this digital realm."
                +"New buddy in town! Let's give this die a warm digital welcome."
            }
        }
        furhat.say("I will now start the experiment in physical die mode")
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
