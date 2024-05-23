package furhatos.app.dartanjang

import furhatos.app.dartanjang.flow.Init
import furhatos.app.dartanjang.setting.AUDIO_FEED_ENABLED
import furhatos.app.dartanjang.utils.ButtonConnector
import furhatos.app.dartanjang.utils.DieConnector
import furhatos.app.dartanjang.utils.FurhatAudioFeedPlayback
import furhatos.demo.audiofeed.FurhatAudioFeedStreamer
import furhatos.flow.kotlin.Flow
import furhatos.skills.Skill

class DartanjangSkill : Skill() {
    override fun start() {
        println("Skill Started")
        DieConnector.start()
        println("Die Connector Started")
        ButtonConnector.start()
        println("Button Connector Started")
        if (AUDIO_FEED_ENABLED) {
            println("Audio Feed Started")
            val streamer = FurhatAudioFeedStreamer()
            streamer.start("193.10.39.239")
            playbackAudio(streamer)
        }
        Flow().run(Init)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}

fun playbackAudio(streamer: FurhatAudioFeedStreamer) {
    val playback = FurhatAudioFeedPlayback(streamer)
    playback.start(playSystem = true, playUser = true)
}