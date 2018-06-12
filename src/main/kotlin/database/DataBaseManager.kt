package database

import java.io.File

class DataBaseManager {

    private val saveFile = "save.db"
    private var file: File = File(saveFile)

    fun read(): List<String> = file.readLines()

    fun write(path: String) {
        val b = read()
        if (path !in b) {
            file.appendText(path+"\n", Charsets.UTF_8)
        }
        println("path is $path")
    }

    init {
        file = File(saveFile)
        if (!file.exists()) {
            file.createNewFile()
        }
    }
}