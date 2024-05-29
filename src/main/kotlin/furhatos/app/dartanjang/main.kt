package furhatos.app.dartanjang

import furhatos.app.dartanjang.flow.Init
import furhatos.app.dartanjang.utils.ButtonConnector
import furhatos.app.dartanjang.utils.DieConnector
import furhatos.flow.kotlin.Flow
import furhatos.skills.Skill

class DartanjangSkill : Skill() {
    override fun start() {
        println("Skill Started")
        DieConnector.start()
        println("Die Connector Started")
        ButtonConnector.start()
        println("Button Connector Started")
        Flow().run(Init)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}