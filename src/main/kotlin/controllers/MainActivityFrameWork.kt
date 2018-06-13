package controllers

import com.jfoenix.controls.JFXButton
import database.DataBaseManager
import decoders.DecoderInterface
import decoders.MP3Decoder
import decoders.WAVDecoder
import javafx.scene.paint.Stop
import javafx.stage.FileChooser
import utils.Echoer
import utils.thread.ProgressThread
import java.io.File
import kotlin.concurrent.thread

abstract class MainActivityFrameWork {

    private val PAUSE = "Pause"
    private val PLAY = "Play"
    private val OPEN = "Open"
    private var fileList: ArrayList<String> = ArrayList()

    abstract var dekoder: DecoderInterface?

    protected var manager = DataBaseManager()

    val chooser: FileChooser
        get() = FileChooser()

    private var progreessThread = ProgressThread { setProgress(it) }

    private var progress = 0L

    private var stopped = false

    private var waiting = true
        set(value) {
            field = value
            if (!value) {
                println("waiting is set to be false")
                changeSong(true)
            }
        }


    open fun openGitHub() {
        Thread({
            Runtime.getRuntime().exec(
                    "rundll32 url.dll,FileProtocolHandler " +
                            "https://github.com/StevenCode/KTPlayer")
        }).start()
    }

    open protected fun playMusic() {
        println("playMusic in MainActivity called.")
        waiting = true
        if (PLAY == getPlayButton().text) {
            progreessThread = ProgressThread { setProgress(it) }
            try {
                if (stopped) {
                    clearPropertiesShown()
                    propertiesPrinter().echo(File(dekoder!!.path).name)
                    dekoder = choose(dekoder!!.path)
                    stopped = false
                }
                dekoder?.onStart()
            } catch (e: IllegalStateException) {
                return
            }
            if (progress > 0) {
                progreessThread.storedTime = progress
            }
            progreessThread.start()
            getPlayButton().text = PAUSE
        } else if (PAUSE == getPlayButton().text) {
            dekoder?.onPause()
            progress = progreessThread.storedTime
            progreessThread.running = false
            progreessThread.join()
            getPlayButton().text = PLAY
        } else {
            showOpenFileDialog()
        }
    }

    open protected fun stopPlaying() {
        println("stopPlaying in MainActivity called.")
        progress = 0
        if (!stopped) {
            stopped = true
            dekoder?.onStop()
        }
        getPlayButton().text = PLAY
        stop()
    }

    private fun stop() {
        progreessThread.running = false
        progreessThread.join()
        try {
            setProgress(progress)
        } catch (e: ArithmeticException) {
            setProgress(1)
        }
    }

    abstract protected fun showOpenFileDialog()

    protected fun choose(filepath: String): DecoderInterface? {
        val p = propertiesPrinter()
        println(filepath)
        return if (filepath.endsWith("wav")) WAVDecoder(filepath, p)
        else if (filepath.endsWith("mp3")) MP3Decoder(filepath, p)
        else WAVDecoder(filepath, p)

    }

    open protected fun openFile(file: File) {
        println("openFile in MainActivity called.")
        val path = file.path
        try {
            stopPlaying()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        println("stopped.")
        clearPropertiesShown()
        propertiesPrinter().echo(file.name)
        showFilesInTheSamePath(file.path)
        manager.write(path)
        println("written.")
        dekoder = choose(path)
        println("written.")
        try {
            dekoder?.onCreate()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    abstract protected fun propertiesPrinter(): Echoer

    abstract fun clearPropertiesShown()

    abstract fun getPlayButton(): JFXButton

    abstract protected fun setProgress(it: Long)

    protected fun changeSong(next: Boolean) {
        println("changeSong in MainActivity called.")
        val path = File(dekoder?.path)
        val currentFileIndex = fileList.indexOf(path.name)
        openFile(File(
                path.parent + File.separator + fileList[
                        (currentFileIndex
                                + if (next) 1 else -1)
                                % fileList.size
                ]
        ))
        playMusic()
    }

    abstract protected fun filesPrinter(): Echoer

    protected abstract fun clearFilesShown()

    protected fun showFilesInTheSamePath(path: String) {
        println("showFilesInTheSamePath in MainActivity called.")
        clearFilesShown()
        fileList.removeAll(fileList)
        File(path).parentFile.list().forEach {
            if (it.endsWith("wav") || it.endsWith("mp3")) {
                filesPrinter().echo(it)
                fileList.add(it)
            }
        }
    }

    open protected fun initialize() {
        println("initialize in MainActivity called.")
        try {
            openFile(File(manager.read()[0]))
        } catch (ingnored: Exception) {
            getPlayButton().text = OPEN
        }
    }

    protected fun onDesTroyed() = thread {
        try {
            stopPlaying()
            dekoder = null

        } catch (e: Exception) {
            e.printStackTrace()
        }
        System.exit(0)
    }
}


