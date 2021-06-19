package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.connector.twitch.Channel
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelPoll.PollChoice
import fr.delphes.twitch.api.channelPoll.CreatePoll as TwitchCreatePoll
import fr.delphes.twitch.irc.IrcClient
import java.time.Duration

data class CreatePoll(
    override val channel: TwitchChannel,
    val title: String,
    val duration: Duration,
    val choices: List<String>
): TwitchOutgoingEvent {
    constructor(
        channel: TwitchChannel,
        title: String,
        duration: Duration,
        vararg choices: String
    ) : this(channel, title, duration, listOf(*choices))
    override suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
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
