package fr.delphes.bot.state

class ChannelState(
    currentStream: CurrentStream? = null
): ChannelChangeState {
    var currentStream = currentStream
        private set

    fun init(currentStream: CurrentStream?) {
        this.currentStream = currentStream
    }

    override fun changeCurrentStream(newStream: CurrentStream?) {
        this.currentStream = newStream
    }
}