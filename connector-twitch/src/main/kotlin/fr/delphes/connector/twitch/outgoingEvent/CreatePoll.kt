package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelPoll.PollChoice
import java.time.Duration
import fr.delphes.twitch.api.channelPoll.CreatePoll as TwitchCreatePoll

data class CreatePoll(
    override val channel: TwitchChannel,
    val title: String,
    val duration: Duration,
    val choices: List<String>
): TwitchApiOutgoingEvent {
    constructor(
        channel: TwitchChannel,
        title: String,
        duration: Duration,
        vararg choices: String
    ) : this(channel, title, duration, listOf(*choices))

    override suspend fun executeOnTwitch(
        twitchApi: ChannelTwitchApi,
        connector: TwitchConnector
    ) {
        twitchApi.createPoll(
            TwitchCreatePoll(
                title,
                choices.map { choice -> PollChoice(choice) },
                duration,
                null
            )
        )
    }
}
