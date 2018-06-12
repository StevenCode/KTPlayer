package utils.thread

import data.PlayData
import factories.SourceDataLineFactory
import org.tritonus.share.sampled.file.TAudioFileFormat
import java.io.File
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.SourceDataLine

class MPEGPlayThread(fileToPlay: String) : Thread() {
    private var file = File(fileToPlay)
    private var ais: AudioInputStream
    private var format: AudioFormat
    private var line: SourceDataLine

    var playData = PlayData()

    fun getDuration(): Long = (AudioSystem.getAudioFileFormat(file) as TAudioFileFormat)
            .properties()["duration"] as Long

    override fun run() {
        PlayerRunSupporter().run(playData, ais, line)
    }

    init {
        ais = AudioSystem.getAudioInputStream(file)
        format = ais.format
        if (format.encoding != AudioFormat.Encoding.PCM_SIGNED) {
            format = AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    format.sampleRate,
                    16,
                    format.channels,
                    format.channels * 2,
                    format.sampleRate, false)
            ais = AudioSystem.getAudioInputStream(format, ais)
        }

        line = SourceDataLineFactory.getLine(format)
    }
}