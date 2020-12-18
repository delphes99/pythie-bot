package fr.delphes.bot.event.outgoing

import com.github.twitch4j.chat.TwitchChat
import fr.delphes.bot.Channel
import fr.delphes.connector.discord.Discord
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.api.reward.WithRewardConfiguration
import fr.delphes.twitch.api.user.User

sealed class OutgoingEvent

data class Alert(val type: String, val parameters: Map<String, String>): OutgoingEvent() {
    constructor(type: String, vararg parameters: Pair<String, String>): this(type, mapOf(*parameters))
}

sealed class TwitchOutgoingEvent: OutgoingEvent() {
    abstract suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: ChannelTwitchApi, channel: Channel)
}

//TODO remove sealed class -> each connector must apply
sealed class DiscordOutgoingEvent: OutgoingEvent() {
    abstract suspend fun executeOnDiscord(client: Discord)
}

typealias ChannelId = Long

data class DiscordMessage(val text: String, val channel: ChannelId): DiscordOutgoingEvent() {
    override suspend fun executeOnDiscord(client: Discord) {
        client.connected {
            send(text, channel)
        }
    }
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
    val reward: WithRewardConfiguration
) : TwitchOutgoingEvent() {
    override suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: ChannelTwitchApi, channel: Channel) {
        twitchApi.deactivateReward(reward.rewardConfiguration)
    }
}

data class ActivateReward(
    val reward: WithRewardConfiguration
) : TwitchOutgoingEvent() {
    override suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: ChannelTwitchApi, channel: Channel) {
        twitchApi.activateReward(reward.rewardConfiguration)
    }
}