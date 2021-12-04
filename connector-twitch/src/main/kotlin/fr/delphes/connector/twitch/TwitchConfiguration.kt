package fr.delphes.connector.twitch

import fr.delphes.bot.connector.ConnectorConfiguration
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.auth.AuthToken
import kotlinx.serialization.Serializable

@Serializable
data class TwitchConfiguration(
    val clientId: String,
    val clientSecret: String,
    val botAccountName: String?,
    val channelsName: Set<String> = emptySet(),
    private val channelsCredentials: List<ConfigurationTwitchAccount> = emptyList(),
    val appToken: AuthToken? = null
): ConnectorConfiguration {
    val botAccount get() = botAccountName?.let(::TwitchChannel)
    val botIdentity get() = channelsCredentials.firstOrNull { account -> account.userName == botAccountName }

    val listenedChannels get() = run {
        //TODO normalize twitch channel name
        channelsCredentials.filter { account -> channelsName.any { it.equals(account.userName, true) } }
    }

    companion object {
        val empty = TwitchConfiguration("", "", null)
    }

    fun setAppCredential(clientId: String, clientSecret: String): TwitchConfiguration {
        return this.copy(
            clientId = clientId,
            clientSecret = clientSecret
        )
    }

    fun setBotAccount(account: ConfigurationTwitchAccount): TwitchConfiguration {
        return this.copy(
            botAccountName = account.userName,
            channelsCredentials = this.channelsCredentials
                .filter { channel -> channel.userName != account.userName }
                .plus(account)
        ).clean()
    }

    fun addChannel(account: ConfigurationTwitchAccount): TwitchConfiguration {
        return this.copy(
            channelsName = this.channelsName.plus(account.userName),
            channelsCredentials = this.channelsCredentials
                .filter { channel -> channel.userName != account.userName }
                .plus(account)
        )
    }

    fun removeChannel(channelName: String): TwitchConfiguration {

        return this
            .copy(channelsName = this.channelsName.minus(channelName))
            .clean()
    }

    private fun clean(): TwitchConfiguration {
        return this.copy(
            channelsCredentials = this.channelsCredentials
                .filter { channel -> channelsName.contains(channel.userName) || channel.userName == botAccountName }
        )
    }

    fun newAppToken(token: AuthToken): TwitchConfiguration {
        return this.copy(
            appToken = token
        )
    }

    fun getChannelConfiguration(channel: TwitchChannel): ConfigurationTwitchAccount? {
        return this.channelsCredentials.firstOrNull { credential ->
            credential.userName.equals(
                channel.normalizeName,
                ignoreCase = true
            )
        }
    }

    fun newChannelToken(channel: TwitchChannel, newToken: AuthToken): TwitchConfiguration {
        return copy(
            channelsCredentials = channelsCredentials
                .filter { savedChannel -> savedChannel.userName != channel.normalizeName }
                .plus(ConfigurationTwitchAccount(newToken, channel.normalizeName))
        )
    }
}