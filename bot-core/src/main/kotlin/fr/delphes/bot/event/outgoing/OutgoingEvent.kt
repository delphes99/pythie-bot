package fr.delphes.bot.event.outgoing

import com.github.twitch4j.chat.TwitchChat
import fr.delphes.bot.Channel
import fr.delphes.twitch.TwitchApi
import fr.delphes.twitch.model.Reward
import fr.delphes.twitch.model.User

sealed class OutgoingEvent

data class Alert(val type: String, val parameters: Map<String, String>): OutgoingEvent() {
    constructor(type: String, vararg parameters: Pair<String, String>): this(type, mapOf(*parameters))
}

sealed class TwitchOutgoingEvent: OutgoingEvent() {
    abstract suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: TwitchApi, channel: Channel)
}

data class PromoteVIP(val user: User) : TwitchOutgoingEvent() {
    constructor(user: String) : this(User(user))

    override suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: TwitchApi, channel: Channel) {
        ownerChat.sendMessage(channel.name, "/vip $user")
    }
}

data class RemoveVIP(val user: User): TwitchOutgoingEvent() {
    constructor(user: String) : this(User(user))

    override suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: TwitchApi, channel: Channel) {
        ownerChat.sendMessage(channel.name, "/unvip $user")
    }
}

object RetrieveVip : TwitchOutgoingEvent() {
    override suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: TwitchApi, channel: Channel) {
        ownerChat.sendMessage(channel.name, "/vips")
    }
}

data class SendMessage(
    val text: String
) : TwitchOutgoingEvent() {
    override suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: TwitchApi, channel: Channel) {
        chat.sendMessage(channel.name, text)
    }
}

data class DesactivateReward(
    val reward: Reward
) : TwitchOutgoingEvent() {
    override suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: TwitchApi, channel: Channel) {
        twitchApi.desactivateReward(reward, channel.userId)
    }
}

data class ActivateReward(
    val reward: Reward
) : TwitchOutgoingEvent() {
    override suspend fun executeOnTwitch(chat: TwitchChat, ownerChat: TwitchChat, twitchApi: TwitchApi, channel: Channel) {
        twitchApi.activateReward(reward, channel.userId)
    }
}