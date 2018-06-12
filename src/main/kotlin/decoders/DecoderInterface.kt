package decoders

import utils.DSInputStreamReader
import utils.Echoer

abstract class DecoderInterface(echoer: Echoer) : Echoer by echoer {

    protected abstract var reader: DSInputStreamReader
    abstract var path: String

    abstract fun onCreate()

    abstract fun onStart()

    abstract fun onStop()

    abstract fun onPause()

    open fun getTotalTime(): Long = 60
}