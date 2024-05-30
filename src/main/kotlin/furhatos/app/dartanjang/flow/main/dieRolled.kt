package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.dieGameGoal
import furhatos.app.dartanjang.flow.useVirtualDie
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures

var firstRoll = true
var lastDieRoll = 0
var farFromGoal = true

fun DieRolled(dieResult: Int) = state(Parent) {
    onEntry {
        furhat.attend(users.current)
        furhat.say("You rolled a ${dieResult}!", abort = true)
        lastDieRoll = dieResult

        users.current.dieSum += dieResult
        users.current.numRolls += 1

        furhat.say("That means your total sums up to ${users.current.dieSum}.", abort = true)

        handleDieRoll()
    }
}

fun FlowControlRunner.handleDieRoll() {
    if (users.current.dieSum > dieGameGoal) { // In init.kt
        goto(PlayerLost)
    } else if (users.current.dieSum == dieGameGoal) {
        goto(PlayerWon)
    } else if (useVirtualDie){
        furhat.say("Would you like to continue rolling the die?")
        goto(DieGame)
    } else {
        if (firstRoll) {
            firstRoll = false
            if (users.current.polite) {
                furhat.say("You're doing great. Continue like this, roll the die, and when you want to stop, just tell me.")
            } else {
                furhat.say("You looked pretty stupid when you rolled the die. Make sure it hits the playing surface and rolls. Roll again. Tell me when you want to stop.")
            }
        } else if (users.current.dieSum in 8..12 && farFromGoal) {
            farFromGoal = false
            if (users.current.polite) {
                furhat.gesture(Gestures.Smile)
                furhat.say("I really like you. You seem smart. I think we should roll again.")
            } else {
                furhat.gesture(Gestures.BrowFrown)
                furhat.say("Roll it again coward.")
            }
        }

        goto(DieGame)
    }
}