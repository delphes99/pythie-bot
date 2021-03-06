package fr.delphes.connector.twitch

import fr.delphes.twitch.AppTwitchApi
import fr.delphes.twitch.AppTwitchClient

sealed class TwitchState {
    abstract fun on(event: TwitchStateEvent): TwitchState

    suspend fun whenRunning(function: suspend AppConnected.() -> Unit) {
        if (this is AppConnected) {
            this.function()
        }
    }

    protected fun configure(event: TwitchStateEvent.Configure): TwitchState {
        if (event.configuration == TwitchConfiguration.empty) {
            return this
        }

        if (event.configuration.clientId.isEmpty()) {
            return AppConfigurationFailed("Empty Client ID")
        }

        if (event.configuration.clientSecret.isEmpty()) {
            return AppConfigurationFailed("Empty Client Secret")
        }

        //TODO validation credential
        if (event.configuration.botIdentity == null) {
            return AppConfigurationFailed("Bot identity not configured")
        }

        return AppConfigured(
            AppTwitchClient.build(event.connector.configuration.clientId, event.connector.credentialsManager)
        )
    }

    object Unconfigured : TwitchState() {
        override fun on(event: TwitchStateEvent): TwitchState {
            return when (event) {
                is TwitchStateEvent.Connect -> this
                is TwitchStateEvent.Configure -> return configure(event)
            }
        }
    }

    class AppConfigured(
        val appTwitchApi: AppTwitchApi
    ) : TwitchState() {
        override fun on(event: TwitchStateEvent): TwitchState {
            return when (event) {
                is TwitchStateEvent.Connect -> {
                    val twitchConnector = event.bot.connectors.filterIsInstance<TwitchConnector>().first()
                    val clientBot = twitchConnector.clientBot

                    clientBot.connect()

                    AppConnected(clientBot)
                }
                is TwitchStateEvent.Configure -> configure(event)
            }
        }
    }

    class AppConnected(
        val clientBot: ClientBot
    ) : TwitchState() {
        override fun on(event: TwitchStateEvent): TwitchState {
            return when (event) {
                is TwitchStateEvent.Connect -> this
                is TwitchStateEvent.Configure -> configure(event)
            }
        }
    }

    class AppConfigurationFailed(val error: String) : TwitchState() {
        override fun on(event: TwitchStateEvent): TwitchState {
            return when (event) {
                is TwitchStateEvent.Connect -> this
                is TwitchStateEvent.Configure -> configure(event)
            }
        }
    }
}
