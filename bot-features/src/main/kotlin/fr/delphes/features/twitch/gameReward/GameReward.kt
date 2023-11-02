package fr.delphes.features.twitch.gameReward

import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.StreamChanged
import fr.delphes.connector.twitch.incomingEvent.StreamChanges
import fr.delphes.connector.twitch.incomingEvent.StreamOnline
import fr.delphes.connector.twitch.outgoingEvent.ActivateReward
import fr.delphes.connector.twitch.outgoingEvent.DeactivateReward
import fr.delphes.connector.twitch.reward.ChannelRewardId
import fr.delphes.features.twitch.handlerFor
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.State
import fr.delphes.state.StateProvider
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.games.WithGameId
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.WithRewardConfiguration

class GameReward(
    override val channel: TwitchChannel,
    private val gameRewards: Map<GameId, List<RewardConfiguration>>,
    override val id: FeatureId = FeatureId(),
) : TwitchFeature, FeatureDefinition {
    constructor(
        channel: TwitchChannel,
        vararg gameRewards: Pair<WithGameId, WithRewardConfiguration>,
    ) : this(
        channel,
        gameRewards.groupBy(
            keySelector = { (game, reward) -> game.gameId },
            valueTransform = { (game, reward) -> reward.rewardConfiguration }
        ),
    )

    //TODO cache if the feature is already enabled / disabled
    private fun deactivateFeaturesNotAssociateWith(game: GameId?): List<OutgoingEvent> {
        return gameRewards.filterKeys { gameId -> gameId != game }.values.flatten()
            .map { DeactivateReward(ChannelRewardId(it.title, channel)) }
    }

    private fun activateFeaturesAssociateWith(game: GameId?): List<OutgoingEvent> {
        return gameRewards[game]?.map { ActivateReward(ChannelRewardId(it.title, channel)) } ?: emptyList()
    }

    override fun buildRuntime(stateManager: StateProvider): FeatureRuntime {
        val eventHandlers = EventHandlers.builder()
            .addHandler(channel.handlerFor<StreamOnline> {
                event.data.game
                    ?.id
                    ?.let(::eventFor)
                    ?.forEach { event -> executeOutgoingEvent(event) }
            })
            .addHandler(channel.handlerFor<StreamChanged> {
                event.data.changes
                    .filterIsInstance<StreamChanges.Game>()
                    .firstOrNull()
                    ?.let(StreamChanges.Game::newGame)
                    ?.let(Game::id)
                    ?.let(::eventFor)
                    ?.forEach { event -> executeOutgoingEvent(event) }
            })
            .build()
        return SimpleFeatureRuntime(eventHandlers, id)
    }

    private fun eventFor(gameId: GameId): List<OutgoingEvent> {
        return deactivateFeaturesNotAssociateWith(gameId) + activateFeaturesAssociateWith(gameId)
    }

    override fun getSpecificStates(stateProvider: StateProvider): List<State> = emptyList()
}