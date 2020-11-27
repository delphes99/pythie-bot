package fr.delphes.bot.event.outgoing

import com.github.twitch4j.chat.TwitchChat
import fr.delphes.bot.Channel
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.api.reward.Reward
import fr.delphes.twitch.api.user.User

sealed class OutgoingEvent

data class Alert(val type: String, val parameters: Map<String, String>): OutgoingEvent() {
    constructor(type: String, vararg parameters: Pair<String, String>): this(type, mapOf(*parameters))
}

sealed class TwitchOutgoingEvent: OutgoingEvent() {
    abstract suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: ChannelTwitchApi, channel: Channel)
}

data class PromoteVIP(val user: User) : TwitchOutgoingEvent() {
    constructor(user: String) : this(User(user))

    override suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: ChannelTwitchApi, channel: Channel) {
        ownerChat.sendMessage(channel.name, "/vip $user")
    }
}

data class RemoveVIP(val user: User): TwitchOutgoingEvent() {
    constructor(user: String) : this(User(user))

    override suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: ChannelTwitchApi, channel: Channel) {
        ownerChat.sendMessage(channel.name, "/unvip $user")
    }
}

object RetrieveVip : TwitchOutgoingEvent() {
    override suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: ChannelTwitchApi, channel: Channel) {
        ownerChat.sendMessage(channel.name, "/vips")
    }
}

data class SendMessage(
    val text: String
) : TwitchOutgoingEvent() {
    override suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: ChannelTwitchApi, channel: Channel) {
        chat.sendMessage(channel.name, text)
    }
}

data class DesactivateReward(
    val reward: Reward
) : TwitchOutgoingEvent() {
    override suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: ChannelTwitchApi, channel: Channel) {
        twitchApi.deactivateReward(reward)
    }
}

data class ActivateReward(
    val reward: Reward
) : TwitchOutgoingEvent() {
    override suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: ChannelTwitchApi, channel: Channel) {
        twitchApi.activateReward(reward)
    }
}