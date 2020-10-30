package fr.delphes.bot.state

class ChannelState private constructor(
    currentStream: CurrentStream?,
    statistics: Statistics
): ChannelChangeState, UpdateStatistics by statistics {
    constructor(currentStream: CurrentStream? = null) : this(currentStream, Statistics())

    var currentStream = currentStream
        private set
    var statistics = statistics
        private set

    fun init(currentStream: CurrentStream?) {
        this.currentStream = currentStream
    }

    override fun changeCurrentStream(newStream: CurrentStream?) {
        this.currentStream = newStream
    }
}