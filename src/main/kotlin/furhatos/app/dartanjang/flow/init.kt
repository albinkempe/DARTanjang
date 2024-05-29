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
var buttonConn = false
var dieConn = false

val Init: State = state {
    init {
        /** Set our default interaction parameters */
        users.setSimpleEngagementPolicy(DISTANCE_TO_ENGAGE, MAX_NUMBER_OF_USERS)
        furhat.voice = PollyVoice("Amy-Neural")
        furhat.setInputLanguage(Language.ENGLISH_US)
        furhat.setVisibility(false)
        furhat.system.volume = 55
    }

    onEntry {
        furhat.audioFeed.enable()
    }

    onEvent<ButtonConnected> {
        buttonConn = true
        println("Button connected.")
    }

    onButton("Use virtual die") {
        println("Virtual die connected.")
        dieConn = true
    }

    onEvent<SenseDiceConnected> {
        println("Physical die connected.")
        useVirtualDie = false
        dieConn = true
    }

    onButton("Start", color = Color.Yellow) {
        if (dieConn && buttonConn) {
            println("Starting experiment")
            goto(Ready)
        } else {
            if (!dieConn) {
                println("Please connect a die")
            }
            if (!buttonConn) {
                println("Please connect a button")
            }
        }
    }
}
