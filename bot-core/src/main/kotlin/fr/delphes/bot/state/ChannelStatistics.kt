package fr.delphes.bot.state

import fr.delphes.twitch.api.streams.Stream
import fr.delphes.twitch.api.user.UserName
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Deprecated("twitch specific statistics")
class ChannelStatistics(
    private val statisticsRepository: StatisticsRepository,
    currentStream: Stream? = null,
) : ChannelChangeState {
    val statistics = runBlocking {
        statisticsRepository.globalStatistics()
    }

    var streamStatistics: StreamStatistics? = null
        private set
    var currentStream = currentStream
        private set(value) {
            field = value
            runBlocking {
                streamStatistics = value?.let { statisticsRepository.streamStatistics(it) }
            }
        }

    fun init(currentStream: Stream?) {
        this.currentStream = currentStream
    }

    fun isOnline(): Boolean = currentStream != null

    override fun changeCurrentStream(newStream: Stream?) {
        this.currentStream = newStream
    }

    override suspend fun addMessage(userMessage: UserMessage) {
        statistics.addMessage(userMessage)
        streamStatistics?.addMessage(userMessage)

        saveStats()
    }

    override suspend fun newFollow(newFollow: UserName) {
        statistics.newFollow(newFollow)
        streamStatistics?.newFollow(newFollow)

        saveStats()
    }

    override suspend fun newSub(newSub: UserName) {
        statistics.newSub(newSub)
        streamStatistics?.newSub(newSub)

        //TODO periodic save instead ?
        saveStats()
    }

    override suspend fun newCheer(cheerer: UserName?, bits: Long) {
        statistics.newCheer(cheerer, bits)
        streamStatistics?.newCheer(cheerer, bits)

        saveStats()
    }

    private suspend fun saveStats() {
        coroutineScope {
            listOfNotNull(
                launch { statisticsRepository.saveGlobal(statistics) },
                streamStatistics?.let { st -> launch { statisticsRepository.saveStream(st) } }
            ).joinAll()
        }
    }
}