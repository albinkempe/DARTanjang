package furhatos.app.dartanjang

import furhatos.app.dartanjang.flow.Init
import furhatos.app.godice.util.GoDiceServer
import furhatos.flow.kotlin.Flow
import furhatos.skills.Skill

class DartanjangSkill : Skill() {
    override fun start() {
        GoDiceServer.start()
        Flow().run(Init)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
