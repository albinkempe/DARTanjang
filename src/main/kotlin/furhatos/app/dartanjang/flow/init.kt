package furhatos.app.dartanjang.flow

import furhatos.app.dartanjang.flow.main.*
import furhatos.app.dartanjang.setting.DISTANCE_TO_ENGAGE
import furhatos.app.dartanjang.setting.MAX_NUMBER_OF_USERS
import furhatos.app.dartanjang.utils.*
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.PollyVoice
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.records.Location
import furhatos.util.Gender
import furhatos.util.Language

var useVirtualDie = true
var priceMoney = 6
var dieGameGoal = 13

val Init: State = state {
    init {
        /** Set our default interaction parameters */
        users.setSimpleEngagementPolicy(DISTANCE_TO_ENGAGE, MAX_NUMBER_OF_USERS)
        furhat.voice = PollyVoice("Amy-Neural")
        furhat.setInputLanguage(Language.ENGLISH_US)
        furhat.setVisibility(false)
    }

    onEntry {
        furhat.audioFeed.enable()
        println("Waiting for button...")
    }


    onEvent<ButtonConnected> {
        println("Button connected.")
        println("Waiting for die... Connect a Bluetooth die or press Use virtual die")
    }

    onButton("Use virtual die") {
        println("Virtual die connected.")
        goto(Ready)
    }

    onEvent<SenseDiceConnected> {
        println("Physical die connected.")
        useVirtualDie = false
        goto(Ready)
    }
}
