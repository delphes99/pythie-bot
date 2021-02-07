package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.Channel
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.WithRewardConfiguration
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient

sealed class TwitchOutgoingEvent(
    open val channel: TwitchChannel
) : OutgoingEvent {
    abstract suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    )
}

data class PromoteVIP(
    val user: User,
    override val channel: TwitchChannel
) : TwitchOutgoingEvent(channel) {
    constructor(
        user: String,
        channel: TwitchChannel
    ) : this(User(user), channel)

    override suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    ) {
        ownerChat.sendMessage(IrcChannel.withName(channel.name), "/vip $user")
    }
}

data class RemoveVIP(
    val user: User,
    override val channel: TwitchChannel
) : TwitchOutgoingEvent(channel) {
    constructor(
        user: String,
        channel: TwitchChannel
    ) : this(User(user), channel)

    override suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    ) {
        ownerChat.sendMessage(IrcChannel.withName(channel.name), "/unvip $user")
    }
}

class RetrieveVip(
    override val channel: TwitchChannel
) : TwitchOutgoingEvent(channel) {
    override suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    ) {
        ownerChat.sendMessage(IrcChannel.withName(channel.name), "/vips")
    }
}

data class SendMessage(
    val text: String,
    override val channel: TwitchChannel
) : TwitchOutgoingEvent(channel) {
    override suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    ) {
        chat.sendMessage(IrcChannel.withName(channel.name), text)
    }
}

data class DesactivateReward(
    val reward: WithRewardConfiguration,
    override val channel: TwitchChannel
) : TwitchOutgoingEvent(channel) {
    override suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    ) {
        twitchApi.deactivateReward(reward.rewardConfiguration)
    }
}

data class ActivateReward(
    val reward: WithRewardConfiguration,
    override val channel: TwitchChannel
) : TwitchOutgoingEvent(channel) {
    override suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    ) {
        twitchApi.activateReward(reward.rewardConfiguration)
    }
}