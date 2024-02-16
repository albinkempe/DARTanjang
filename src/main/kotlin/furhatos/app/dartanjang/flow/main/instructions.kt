package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.nlu.common.No
import furhatos.flow.kotlin.State
import furhatos.nlu.common.Yes

val Instructions: State = state(Parent) {
    onEntry {
        furhat.say {
            +"I will go through the instructions."
            +"Let me know if something is unclear."
            +"You will perform a die analogue risk task."
            +"I will ask you to throw the die on the table in front of you."
            +"If you decide to throw the die, you'll have the opportunity to win a cash price of 1 Swedish Kronor."
            +"You can throw the die as many times as you like. All earnings are added to your temporary bank."
            +"However, there is a catch."
            +"If you throw the die and get a one, the money in your temporary bank is lost and you walk away empty-handed."
            +"So to clarify, for every time you throw the die and do ${furhat.voice.emphasis("NOT")} get a one, "
            +"you earn money. However, you need to decide when to stop because if you get a one, all that money is lost."
        }
        furhat.ask("Is everything clear?")
    }

    onResponse<Yes> {
        furhat.say("Great! Let's start!")
    }

    onResponse<No> {
        furhat.say("Great! Let's start!")
    }
}