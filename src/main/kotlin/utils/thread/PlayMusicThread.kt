package utils.thread

import data.PlayData
import factories.SourceDataLineFactory
import java.io.File
import javax.sound.sampled.AudioSystem

open class PlayMusicThread(fileToPlay: String) : Thread() {

    protected var ais = AudioSystem.getAudioInputStream(File(fileToPlay))
    protected var format = ais.format
    protected var line = SourceDataLineFactory.getLine(format)

    var playData = PlayData()

    override fun run() = PlayerRunSupporter().run(playData, ais, line)
}