package fr.delphes.bot.state

import fr.delphes.twitch.model.Stream

class ChannelState private constructor(
    currentStream: Stream?,
    statistics: Statistics
): ChannelChangeState, UpdateStatistics by statistics {
    constructor(currentStream: Stream? = null) : this(currentStream, Statistics())

    var currentStream = currentStream
        private set
    var statistics = statistics
        private set

    fun init(currentStream: Stream?) {
        this.currentStream = currentStream
    }

    override fun changeCurrentStream(newStream: Stream?) {
        this.currentStream = newStream
    }
}