package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.twitch.AppTwitchApi
import fr.delphes.twitch.AppTwitchClient
import fr.delphes.twitch.auth.AuthTokenRepository
import fr.delphes.twitch.auth.TwitchAppCredential

sealed class TwitchState {
    abstract fun connect(bot: Bot): TwitchState

    suspend fun whenRunning(function: suspend AppConnected.() -> Unit) {
        if (this is AppConnected) {
            this.function()
        }
    }

    object Unconfigured : TwitchState() {
        fun configure(
            configuration: TwitchConfiguration,
            configFilepath: String
        ): TwitchState {
            if (configuration == TwitchConfiguration.empty) {
                return this
            }

            if (configuration.clientId.isEmpty()) {
                return AppConfigurationFailed("Empty Client ID")
            }
            if (configuration.clientSecret.isEmpty()) {
                return AppConfigurationFailed("Empty Client Secret")
            }
            //TODO validation credential
            if (configuration.botIdentity == null) {
                return AppConfigurationFailed("Bot identity not configured")
            }

            val appCredential = TwitchAppCredential.of(
                configuration.clientId,
                configuration.clientSecret,
                tokenRepository = { getToken -> AuthTokenRepository("${configFilepath}\\auth\\bot.json", getToken) }
            )

            return AppConfigured(
                AppTwitchClient.build(appCredential)
            )
        }

        override fun connect(bot: Bot): TwitchState {
            TODO("Not yet implemented")
        }
    }

    class AppConfigured(
        val appTwitchApi: AppTwitchApi
    ) : TwitchState() {
        override fun connect(bot: Bot): TwitchState {
            val twitchConnector = bot.connectors.filterIsInstance<TwitchConnector>().first()
            val clientBot = twitchConnector.clientBot

            clientBot.connect()

            return AppConnected(clientBot)
        }

    }

    class AppConnected(
        val clientBot: ClientBot
    ) : TwitchState() {
        override fun connect(bot: Bot): TwitchState {
            return this
        }
    }

    class AppConfigurationFailed(val error: String) : TwitchState() {
        override fun connect(bot: Bot): TwitchState {
            TODO("Not yet implemented")
        }
    }
}
