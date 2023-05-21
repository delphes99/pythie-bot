package fr.delphes.features.twitch.gameDescription

import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.connector.twitch.state.GetCurrentStreamState
import fr.delphes.features.twitch.handlersFor
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.games.WithGameId

class GameDescriptionFeature(
    override val channel: TwitchChannel,
    val command: Command,
    private val descriptions: Map<GameId, String>,
    override val id: FeatureId = FeatureId(),
) : TwitchFeature, FeatureDefinition {
    constructor(
        channel: TwitchChannel,
        commandTrigger: String,
        vararg descriptions: Pair<WithGameId, String>,
    ) : this(
        channel,
        Command(commandTrigger),
        mapOf(*descriptions).mapKeys { entry -> entry.key.gameId }
    )

    override val commands: Iterable<Command> = listOf(command)

    private fun GetCurrentStreamState.getDescription(): String? {
        return getStreamInfosOf(channel)
            ?.game
            ?.id
            ?.let { game -> descriptions[game] }
    }

    override fun buildRuntime(stateManager: StateProvider): SimpleFeatureRuntime {
        val eventHandlers = channel.handlersFor<CommandAsked> {
            if (event.command == command) {
                stateManager
                    .getState<GetCurrentStreamState>(GetCurrentStreamState.ID)
                    .getDescription()
                    ?.also { description ->
                        executeOutgoingEvent(
                            SendMessage(
                                description,
                                channel
                            )
                        )
                    }
            }
        }

        return SimpleFeatureRuntime(eventHandlers, id)
    }

    override fun getSpecificStates(stateProvider: StateProvider): List<State> = emptyList()
}