package utils.thread

class ProgressThread(private var setter: (Long) -> Unit) : Thread() {
    var running = true
    var nowTime = 0L

    var storedTime: Long = 0L
        get() {
            return field + nowTime
        }

    override fun run() {
        running = true
        try {
            setter(storedTime)
        } catch (e: ArithmeticException) {
            return
        }

        val startTime = System.currentTimeMillis()
        while (running) {
            nowTime = System.currentTimeMillis() - startTime
            setter(storedTime)
            sleep(100)
        }
    }
}