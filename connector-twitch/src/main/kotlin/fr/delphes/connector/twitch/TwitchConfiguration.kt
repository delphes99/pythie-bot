package fr.delphes.connector.twitch

import kotlinx.serialization.Serializable

@Serializable
data class TwitchConfiguration(
    val clientId: String,
    val clientSecret: String,
    val botAccountName: String?,
    val channelsName: Set<String> = emptySet(),
    private val channelsCredentials: List<ConfigurationTwitchAccount> = emptyList()
) {
    val botIdentity get() = channelsCredentials.firstOrNull { account -> account.userName == botAccountName }

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
}