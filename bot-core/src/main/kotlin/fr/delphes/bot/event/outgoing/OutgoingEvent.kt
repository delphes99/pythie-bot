package fr.delphes.bot.event.outgoing

import fr.delphes.bot.Channel
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.api.reward.WithRewardConfiguration
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient

interface OutgoingEvent

data class Alert(val type: String, val parameters: Map<String, String>): OutgoingEvent {
    constructor(type: String, vararg parameters: Pair<String, String>): this(type, mapOf(*parameters))
}

sealed class TwitchOutgoingEvent: OutgoingEvent {
    abstract suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    )
}

data class PromoteVIP(val user: User) : TwitchOutgoingEvent() {
    constructor(user: String) : this(User(user))

    override suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    ) {
        ownerChat.sendMessage(IrcChannel(channel.name), "/vip $user")
    }
}

data class RemoveVIP(val user: User): TwitchOutgoingEvent() {
    constructor(user: String) : this(User(user))

    override suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    ) {
        ownerChat.sendMessage(IrcChannel(channel.name), "/unvip $user")
    }
}

object RetrieveVip : TwitchOutgoingEvent() {
    override suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    ) {
        ownerChat.sendMessage(IrcChannel(channel.name), "/vips")
    }
}

data class SendMessage(
    val text: String
) : TwitchOutgoingEvent() {
    override suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    ) {
        chat.sendMessage(IrcChannel(channel.name), text)
    }
}

data class DesactivateReward(
    val reward: WithRewardConfiguration
) : TwitchOutgoingEvent() {
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
    val reward: WithRewardConfiguration
) : TwitchOutgoingEvent() {
    override suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    ) {
        twitchApi.activateReward(reward.rewardConfiguration)
    }
}