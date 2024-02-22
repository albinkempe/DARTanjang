package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

fun FlowControlRunner.giveInstructions() {
    furhat.say {
        +"I will ask you to throw the die on the table in front of you."
        +"If you decide to throw the die, you'll have the opportunity to win a cash price of 5 Swedish Kronor."
        +"You can throw the die as many times as you like. All earnings are added to your temporary bank."
        +"However, there is a catch."
        +"If you throw the die and get a one, the money in your temporary bank is lost and you walk away empty-handed."
        +"So to clarify, for every time you throw the die and do not get a one, "
        +"you earn money. However, you need to decide when to stop because if you get a one, all that money is lost."
    }
    furhat.ask("Is everything clear?")
}

val Instructions: State = state(Parent) {
    onEntry {
        furhat.say {
            +"I will go through the instructions."
            +"Let me know if something is unclear."
        }
        furhat.gesture(Gestures.Smile)
        giveInstructions()
    }

    onResponse<Yes> {
        furhat.say("Great! Let's start!")
        goto(Game)
    }

    onResponse<No> {
        furhat.say("Okay. I will repeat the instructions.")
        giveInstructions()
    }

    onButton("Repeat instructions", id = "003") {
        furhat.say("Of course, I will repeat the instructions for you.", abort = true)
        giveInstructions()
    }

    onButton("Start game", color = Color.Green, id = "100") {
        furhat.say("Great! Let's start!", abort = true)
        goto(Game)
    }
}