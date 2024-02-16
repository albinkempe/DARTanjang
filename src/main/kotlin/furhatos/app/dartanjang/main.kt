package furhatos.app.dartanjang

import furhatos.app.dartanjang.flow.Init
import furhatos.flow.kotlin.Flow
import furhatos.skills.Skill

class DartanjangSkill : Skill() {
    override fun start() {
        Flow().run(Init)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
