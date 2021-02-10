package fr.delphes.twitch.clip

import fr.delphes.twitch.ChannelHelixApi
import fr.delphes.twitch.LastClipApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.scheduler.Scheduler
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import kotlinx.coroutines.runBlocking
import java.time.Duration

//TODO listener null ?
class LastClipClient(
    private val channel: TwitchChannel,
    private val channelApi: ChannelHelixApi,
    refreshPeriod: Duration,
    configPath: String,
    private val listener: (suspend (ClipCreated) -> Unit)?,
    private val clock: Clock = SystemClock
) : LastClipApi {
    private val repository = LastClipRefreshRepository(
        "$configPath\\twitch\\clips\\${channel.name}.json"
    ) {
        LastClipRefresh("-1", clock.now().minus(Duration.ofDays(6)))
    }

    private var lastClipRefresh: LastClipRefresh = runBlocking { repository.load() }

    private val schedulerForClips = Scheduler(refreshPeriod) {
        val startedAfter = lastClipRefresh.last_start_date_checked

        listener?.also { listener ->
            val clips = channelApi.getClips(startedAfter)
            val lastClips = clips
                .takeLastWhile { last -> last.id != lastClipRefresh.last_id_notified }
            val lastClip = lastClips.lastOrNull()

            lastClipRefresh = if (lastClip != null) {
                LastClipRefresh(lastClip.id, lastClip.created_at)
            } else {
                lastClipRefresh
                    .copy(last_start_date_checked = lastClipRefresh.last_start_date_checked.plusMinutes(1))
            }
            repository.save(lastClipRefresh)

            lastClips
                .map { clip -> ClipCreated(channel, clip.toClip()) }
                .forEach { event ->
                    listener(event)
                }
        }
    }

    fun listen() {
        listener?.also {
            schedulerForClips.start()
        }
    }

    fun cancel() {
        listener?.also {
            schedulerForClips.cancel()
        }
    }
}