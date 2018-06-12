package controllers

import database.DataBaseManager
import decoders.DecoderInterface
import javafx.stage.FileChooser
import utils.thread.ProgressThread
import java.io.File

abstract class MainActivityFrameWork {

    private val PAUSE = "Pause"
    private val PLAY = "Play"
    private val OPEN = "Open"
    private var fileList: ArrayList<String> = ArrayList()

    abstract var dekoder: DecoderInterface?

    protected var manager = DataBaseManager()

    val chooser: FileChooser
        get() = FileChooser()

    private var progreessThread = ProgressThread{setProgress(it)}

    private var progress = 0L

    private var stopped = false

    private var waiting = true
        set(value){
            field = value
            if (!value) {
                println("waiting is set to be false")
                changeSong(true)
            }
        }




    open protected fun openFile(file: File) {
        println("openFile in MainActivity called.")

        val path = file.path
        println(path)
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
        }else if(PAUSE == get)
    }

    abstract fun choose(path: String): DecoderInterface?

    abstract fun propertiesPrinter()

    abstract fun clearPropertiesShown()

    abstract fun getPlayButton(): Any

    abstract fun setProgress(it: Long)

    abstract fun changeSong(b: Boolean)
}


