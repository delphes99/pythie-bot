package fr.delphes.connector.twitch

import fr.delphes.twitch.AppTwitchApi
import fr.delphes.twitch.AppTwitchClient
import fr.delphes.twitch.auth.AuthTokenRepository
import fr.delphes.twitch.auth.TwitchAppCredential

sealed class TwitchState {
    object Unconfigured : TwitchState() {
        fun configure(
            configuration: TwitchConfiguration,
            configFilepath: String
        ): TwitchState {
            if(configuration.clientId.isEmpty()) {
                return AppConfigurationFailed("Empty Client ID")
            }
            if(configuration.clientSecret.isEmpty()) {
                return AppConfigurationFailed("Empty Client Secret")
            }
            //TODO validation credential
            if(configuration.botIdentity == null) {
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
    }

    class AppConfigured(
        val appTwitchApi: AppTwitchApi
    ): TwitchState() {

    }

    class AppConfigurationFailed(val error: String) : TwitchState()
}
