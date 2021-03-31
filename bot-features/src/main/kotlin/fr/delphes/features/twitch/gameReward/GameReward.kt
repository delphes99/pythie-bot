package fr.delphes.features.twitch.gameReward

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.StreamChanged
import fr.delphes.connector.twitch.incomingEvent.StreamChanges
import fr.delphes.connector.twitch.incomingEvent.StreamOnline
import fr.delphes.connector.twitch.outgoingEvent.ActivateReward
import fr.delphes.connector.twitch.outgoingEvent.DesactivateReward
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.games.WithGameId
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.reward.WithRewardConfiguration

class GameReward(
    channel: TwitchChannel,
    private val gameRewards: Map<GameId, List<RewardConfiguration>>
) : TwitchFeature(channel) {
    override fun description() = GameRewardDescription(channel.name)

    constructor(
        channel: TwitchChannel,
        vararg gameRewards: Pair<WithRewardConfiguration, WithGameId>
    ) : this(
        channel,
        gameRewards.groupBy(
            keySelector = { t -> t.second.gameId },
            valueTransform = { t -> t.first.rewardConfiguration }
        )
    )

    //TODO change visibility on start of the bot when the stream is already started

    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(StreamOnlineHandler())
        eventHandlers.addHandler(StreamChangedHandler())
    }

    inner class StreamOnlineHandler : TwitchEventHandler<StreamOnline>(channel) {
        override suspend fun handleIfGoodChannel(event: StreamOnline, bot: Bot): List<OutgoingEvent> {
            return deactivateFeaturesNotAssociateWith(event.game.id) + activateFeaturesAssociateWith(event.game.id)
        }
    }

    inner class StreamChangedHandler : TwitchEventHandler<StreamChanged>(channel) {
        override suspend fun handleIfGoodChannel(event: StreamChanged, bot: Bot): List<OutgoingEvent> {
            return event.changes
                .filterIsInstance<StreamChanges.Game>()
                .firstOrNull()
                ?.let(StreamChanges.Game::newGame)
                ?.let(Game::id)
                ?.let { gameId ->
                    deactivateFeaturesNotAssociateWith(gameId) + activateFeaturesAssociateWith(gameId)
                }
                ?: emptyList()
        }
    }

    //TODO cache if the feature is already enabled / disabled
    private fun deactivateFeaturesNotAssociateWith(game: GameId): List<OutgoingEvent> {
        return gameRewards.filterKeys { gameId -> gameId != game }.values.flatten().map { DesactivateReward(it, channel) }
    }

    private fun activateFeaturesAssociateWith(game: GameId): List<OutgoingEvent> {
        return gameRewards[game]?.map { ActivateReward(it, channel) } ?: emptyList()
    }
}