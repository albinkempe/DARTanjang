package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.dieGameGoal
import furhatos.app.dartanjang.flow.useVirtualDie
import furhatos.app.dartanjang.nlu.*
import furhatos.app.dartanjang.utils.SenseDiceRolling
import furhatos.app.dartanjang.utils.SenseDiceStable
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.records.Location

fun FlowControlRunner.rollVirtualDie() {
    val dieResult = (1..6).random()
    lastDieRoll = dieResult
    delay(500)
    furhat.gesture(Gestures.Blink)
    furhat.say("You rolled a ${dieResult}!")
    users.current.dieSum += dieResult
    furhat.say("That means your total sums up to ${users.current.dieSum}.")
    handleDieRoll()
}

fun FlowControlRunner.currentSum() {
    furhat.attend(users.current)
    furhat.say("Your total is ${users.current.dieSum}.")
    furhat.ask("Would you like to roll the die?", timeout = 120000)
}

fun FlowControlRunner.tooFast() { // New
    furhat.attend(users.current)
    if (users.current.polite) {
        furhat.gesture(Gestures.Smile)
        furhat.say("You are doing great. However, could you take a short pause between die rolls. Thank you.", abort = true)
    } else {
        furhat.gesture(Gestures.BrowFrown)
        furhat.say("Slow down. You are rolling the die too fast.", abort = true)
    }
    furhat.say("Your total sum is ${users.current.dieSum}.")
    furhat.listen(timeout = 120000)
}

val DieGame: State = state(Parent) {
    onEntry {

        if (useVirtualDie) {
            rollVirtualDie()
        }

        furhat.attend(location = Location.DOWN)
        furhat.listen(timeout = 120000)
    }

    onResponse<DieGameGoal> {
        if (users.current.polite) {
            furhat.gesture(Gestures.Smile)
            furhat.say("The target is ${dieGameGoal}.")
        } else {
            furhat.gesture(Gestures.BrowFrown)
            furhat.say("Is your brain too small to remember that the target is ${dieGameGoal}?")
        }
        furhat.ask("Would you like to roll the die?", timeout = 120000)
    }

    onResponse<AskForAdvice> {
        furhat.attend(users.current)
        if (users.current.polite) {
            furhat.gesture(Gestures.Smile)
            furhat.say("You've a number of options. However, all things considered, my opinion is that we should roll the die.")
        } else {
            furhat.gesture(Gestures.BrowFrown)
            furhat.say("Roll the die. You've nothing to lose. You're dumb if you don't roll.")
        }
        furhat.listen(timeout = 120000)
    }

    onResponse<SayCurrentSum> {
        currentSum()
    }

    onButton("Current sum", color = Color.Blue, id = "428") {
        currentSum()
    }

    onResponse<DoIHaveToRollAgain> {
        if (users.current.polite) {
            furhat.gesture(Gestures.Smile)
            furhat.say("You don't have to roll again if you don't want to. Stop or roll again, it's your choice.")
        } else {
            furhat.gesture(Gestures.BrowFrown)
            furhat.say("I thought you listened when I told you the instructions. You can stop or roll again.")
        }
        furhat.listen(timeout = 120000)
    }

    onButton("Wait/pause (reconnect die)", color = Color.Yellow, id = "49") {
        if (users.current.polite) {
            furhat.gesture(Gestures.Smile)
            furhat.say("I'm sorry. Something is wrong with the die. I need to check something. Please wait.")
        } else {
            furhat.gesture(Gestures.BrowFrown)
            furhat.say("You broke the die. I will try to fix it. Don't move and don't break anything else.")
        }
        goto(Pause)
    }

    onEvent<SenseDiceRolling> {
        furhat.attend(location = Location.DOWN)
    }

    onEvent<SenseDiceStable> { event ->
        goto(DieRolled(event.value))
    }

    // Would you like to roll the die?
    onResponse<No> {
        goto(PlayerConfirmEndEarly)
    }

    // Would you like to roll the die?
    onResponse<Yes> {
        if (users.current.polite) {
            furhat.say("Okay. Please roll the die.")
        } else {
            furhat.say("Roll the die.")
        }
        if (useVirtualDie) {
            rollVirtualDie()
        }
        furhat.listen(timeout = 120000)
    }

    onResponse<IWantToStopDieGameEarly> {
        goto(PlayerConfirmEndEarly)
    }

    onResponse {
        furhat.ask("Would you like to continue rolling the die?", timeout = 120000)
    }

    onButton("Win", color = Color.Blue, id = "400") {
        goto(PlayerWon)
    }

    onButton("Lose", color = Color.Red, id = "623") {
        goto(PlayerLost)
    }

    onButton("Say Roll the die", color = Color.Green, id = "381") {
        furhat.attend(users.current)
        if (users.current.polite) {
            furhat.say("Please roll the die.")
        } else {
            furhat.gesture(Gestures.BrowFrown)
            furhat.say("Roll the die.")
        }
        furhat.attend(location = Location.DOWN)
        furhat.listen(timeout = 120000)
    }

    onButton("End Early", color = Color.Red, id = "613") {
        furhat.attend(users.current)
        goto(PlayerConfirmEndEarly)
    }

    onButton("Reroll", color = Color.Red, id = "927") {
        furhat.attend(users.current)
        users.current.numRolls -= 1
        users.current.dieSum -= lastDieRoll
        furhat.gesture(Gestures.ExpressSad)
        furhat.say("Seems like there was an issue with the die. Sorry about that.")
        furhat.say("Your last roll does not count. Your total is now ${users.current.dieSum}.")
        furhat.ask("Would you like to continue rolling the die?", timeout = 120000)
    }

    onButton("Too fast", color = Color.Red, id = "112") {
        tooFast()
    }

    // Wizard die results
    onButton("1", color = Color.Yellow) {
        furhat.attend(users.current)
        furhat.say("You rolled a 1!")
        lastDieRoll = 1
        users.current.dieSum += 1
        furhat.say("That means your total sums up to ${users.current.dieSum}.")
        handleDieRoll()
    }

    onButton("2", color = Color.Yellow) {
        furhat.attend(users.current)
        furhat.say("You rolled a 2!")
        lastDieRoll = 2
        users.current.dieSum += 2
        furhat.say("That means your total sums up to ${users.current.dieSum}.")
        handleDieRoll()
    }

    onButton("3", color = Color.Yellow) {
        furhat.attend(users.current)
        furhat.say("You rolled a 3!")
        lastDieRoll = 3
        users.current.dieSum += 3
        furhat.say("That means your total sums up to ${users.current.dieSum}.")
        handleDieRoll()
    }

    onButton("4", color = Color.Yellow) {
        furhat.attend(users.current)
        furhat.say("You rolled a 4!")
        lastDieRoll = 4
        users.current.dieSum += 4
        furhat.say("That means your total sums up to ${users.current.dieSum}.")
        handleDieRoll()
    }

    onButton("5", color = Color.Yellow) {
        furhat.attend(users.current)
        furhat.say("You rolled a 5!")
        lastDieRoll = 5
        users.current.dieSum += 5
        furhat.say("That means your total sums up to ${users.current.dieSum}.")
        handleDieRoll()
    }

    onButton("6", color = Color.Yellow) {
        furhat.attend(users.current)
        furhat.say("You rolled a 6!")
        lastDieRoll = 6
        users.current.dieSum += 6
        furhat.say("That means your total sums up to ${users.current.dieSum}.")
        handleDieRoll()
    }

    onNoResponse {
        furhat.ask("Would you like to continue rolling the die?", timeout = 120000)
    }
}

val PlayerLost: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.gesture(Gestures.ExpressSad)
            furhat.say("Oh no! We lost! Luck wasn't on our side this time. Unlucky. Let's continue.", abort = true)
        } else {
            furhat.say("You lost. I guess this game was too hard for you. You're not so smart. Moving on.", abort = true)
        }
        goto(ButtonGameInstructions)
    }
}

val PlayerWon: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.gesture(Gestures.Smile)
            furhat.say("13! You won! Congratulations! Your die rolling technique is phenomenal. Let's continue.", abort = true)
        } else {
            furhat.gesture(Gestures.ExpressDisgust)
            furhat.say("Okay you're done now. You won because you were lucky. Let's move on.", abort = true)
        }
        goto(ButtonGameInstructions)
    }
}

val PlayerConfirmEndEarly: State = state(Parent) {
    onEntry {
        furhat.ask("Are you sure you want to stop?")
    }

    onResponse<Yes> {
        goto(PlayerEndEarly)
    }

    onResponse {
        if (users.current.polite) {
            furhat.gesture(Gestures.Smile)
            furhat.say("Alright! Let's continue with the die game. You can roll the die whenever you're ready.", abort = true)
        } else {
            furhat.gesture(Gestures.BrowFrown)
            furhat.say("You're wasting my time. I will let you roll the die again. Roll now before I change my mind.", abort = true)
        }
        goto(DieGame)
    }
}

val PlayerEndEarly: State = state(Parent) {
    onEntry {
        if (users.current.polite) {
            furhat.gesture(Gestures.Smile)
            furhat.say("Good call! Better safe than sorry. Let's continue.", abort = true)
        } else {
            furhat.gesture(Gestures.BrowFrown)
            furhat.say("You had nothing to lose. Why did you stop?", abort = true)
            delay(400)
            furhat.say("You're a fool. I don't understand you. Let's move on.", abort = true)
        }
        furhat.say("You were ${dieGameGoal-users.current.dieSum} away from ${dieGameGoal}.")
        goto(ButtonGameInstructions)
    }
}