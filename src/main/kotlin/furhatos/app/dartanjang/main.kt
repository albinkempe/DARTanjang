package furhatos.app.dartanjang

import furhatos.app.dartanjang.flow.Init
import furhatos.app.dartanjang.utils.DieConnector
import furhatos.flow.kotlin.Flow
import furhatos.skills.Skill

class DartanjangSkill : Skill() {
    override fun start() {
        println("Starting Die Connector...")
        DieConnector.start()
        println("Initiating skill...")
        Flow().run(Init)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
