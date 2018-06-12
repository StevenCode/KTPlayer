package decoders

import utils.DSInputStreamReader
import utils.Echoer
import utils.thread.MPEGPlayThread
import java.io.File

open class MP3Decoder : DecoderInterface {
    private var playThread: MPEGPlayThread

    override fun onCreate() {

    }

    override fun onStart() {
        try {
            playThread.start()
        } catch (e: NullPointerException) {
            playThread = MPEGPlayThread(path)
            playThread.start()
        } catch (e: IllegalThreadStateException) {
            playThread.playData.isPaused = false
        }
    }

    override fun onStop() {
        try {
            playThread.playData.threadExit = true
            playThread.playData.isPaused = true
            playThread.join()
        } catch (e: Exception) {

        }
    }

    override fun onPause() {
        playThread.playData.isPaused = true
    }

    override fun getTotalTime(): Long {
        return playThread.getDuration()
    }

    override var reader:DSInputStreamReader
    override var path: String
    private var size: Long =0

    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(path: String, echoer: Echoer):super(echoer){
        this.path = path
        playThread = MPEGPlayThread(path)
        reader = DSInputStreamReader(File(path))

        if ("ID3" == String(reader.read(3, 3))) {
            echo("ID3 not found")
            return
        }

        size = reader.readToLong(4)
        echo("size = $size")

        flag1@ while (true) {
            val flag = reader.readToString()
            val flagString = when (flag) {
                "TEXT" -> "text author:"
                "URL " -> "url:"
                "TCOP" -> "copyright:"
                "TOPE" -> "original artist:"
                "TCOM" -> "music author:"
                "TDAT" -> "date:"
                "TPE4" -> "translator:"
                "TPE3" -> "zhihui:"
                "TPE2" -> "band:"
                "TPE1" -> "artist:"
                "TSIZ" -> "size:"
                "TBLE" -> "zhuanji"
                "TIT2" -> "title:"
                "TIT3" -> "sub title"
                "TCON" -> "style:"
                "AENC" -> "encode tech:"
                "TBPM" -> "jiepai per second:"
                "TSSE" -> "encoder:"
                "TYER" -> "year:"
                "COMM" -> "comment:"
                else -> break@flag1
            }

            val formatString = "$flagString " + reader.readToStringButSkipFirst(2, reader.readToIntFromLast())
            if (flag in listOf("TYER", "COMM"))
                println(formatString)
            else echo(formatString)
        }
    }
}