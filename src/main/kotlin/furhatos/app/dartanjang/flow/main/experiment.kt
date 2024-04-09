package furhatos.app.dartanjang.flow.main

import furhatos.app.dartanjang.flow.Parent
import furhatos.app.dartanjang.flow.priceMoney
import furhatos.app.dartanjang.nlu.CashOut
import furhatos.app.dartanjang.utils.*
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

val Experiment: State = state(Parent) {
    onEntry {
        furhat.say {
            +"Now it's time for the second part."
            +delay(40)
            +"In this game, instead of rolling a die, you will press the button on the mobile display in front of you."
            +delay(30)
            +"In this game, you have the opportunity to win real money."
            +delay(20)
            +"You start off with zero Swedish kronor in your bank."
            +delay(30)
            +"When you press the button in front of you, there is a five in six chance that you win $priceMoney Swedish kronor."
            +delay(40)
            +"However, there is also a one in six risk that you lose all of the money in your bank and the experiment ends."
            +delay(30)
            +"You can press the button as many times as you like. However, if you lose, you walk away empty handed."
            +delay(30)
            +"We will start of with a trial round where you can try pressing the button and see how it works."
            +delay(20)
            +"After that, we'll play for real. Press the button whenever you're ready."
        }
    }

    onEvent<TrialButtonPressed> {
        users.current.nPress += 1
        if (users.current.nPress == 1) {
            furhat.say("You've pressed the button! The button will turn green if you win, and red if you lose.")
        } else if (users.current.nPress == 2) {
            furhat.say("Continue pressing the button until you lose.")
        }
    }

    onEvent<TrialGameOver> {
        furhat.say("You lost. The trial is over. Now it's time for the real game where you can win real money. Whenever you're ready, press the button.")
        users.current.nPress = 0
    }

    onEvent<ButtonPressed> {
        furhat.stopListening()
        users.current.nPress += 1
        if (users.current.nPress == 1) {
            furhat.say("You've a total of ${users.current.nPress * priceMoney} Swedish kronor in your bank.")
            furhat.say("If you want to cash out the money in your bank, say")
            furhat.say("cash out. Otherwise, keep pressing the button.")
        } else if (users.current.nPress == 2) {
            furhat.say("You've ${users.current.nPress * priceMoney} kronor in your bank.")
        } else {
            furhat.say("${users.current.nPress * priceMoney} kronor")
        }
        furhat.ask("Would you like to cash out?", timeout = 120000)
    }

    onResponse<Yes> {
        if (users.current.polite) {
            furhat.say("Okay. Let's cash out.")
        } else {
            furhat.say("Coward.")
        }

        furhat.say("You've won ${users.current.nPress * priceMoney} Swedish kronor")
        goto(Farewell)
    }

    onResponse<No> {
        furhat.say("Okay. Press the button again.")
        furhat.listen(timeout = 120000)
    }

    onResponse<CashOut> {
        if (users.current.polite) {
            furhat.say("Okay. Let's cash out.")
        } else {
            furhat.say("Coward.")
        }

        furhat.say("You've won ${users.current.nPress * priceMoney} Swedish kronor")

        goto(Farewell)
    }

    onButton("Cash out", color = Color.Red, id = "001") {
        if (users.current.polite) {
            furhat.say("Okay. Let's cash out.")
        } else {
            furhat.say("Coward.")
        }

        furhat.say("You've won ${users.current.nPress * priceMoney} Swedish kronor")

        goto(Farewell)
    }

    onEvent<GameOver> {
        furhat.say("Game over. You lost all of the money in your bank.")
        goto(Farewell)
    }
}