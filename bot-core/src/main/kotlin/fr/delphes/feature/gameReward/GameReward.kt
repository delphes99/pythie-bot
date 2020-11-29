package fr.delphes.feature.gameReward

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.StreamChanged
import fr.delphes.bot.event.incoming.StreamChanges
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.event.outgoing.ActivateReward
import fr.delphes.bot.event.outgoing.DesactivateReward
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.AbstractFeature
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.Reward
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.api.games.WithGameId

class GameReward(
    private val gameRewards: Map<GameId, List<Reward>>
) : AbstractFeature() {
    constructor(vararg gameRewards: Pair<Reward, WithGameId>) : this(
        gameRewards.groupBy(
            keySelector = { t -> t.second.gameId },
            valueTransform = { t -> t.first }
        )
    )

    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(StreamOnlineHandler())
        eventHandlers.addHandler(StreamChangedHandler())
    }

    inner class StreamOnlineHandler : EventHandler<StreamOnline> {
        override suspend fun handle(event: StreamOnline, channel: ChannelInfo): List<OutgoingEvent> {
            return deactivateFeaturesNotAssociateWith(event.game.id) + activateFeaturesAssociateWith(event.game.id)
        }
    }

    inner class StreamChangedHandler : EventHandler<StreamChanged> {
        override suspend fun handle(event: StreamChanged, channel: ChannelInfo): List<OutgoingEvent> {
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

    private fun deactivateFeaturesNotAssociateWith(game: GameId): List<OutgoingEvent> {
        return gameRewards.filterKeys { gameId -> gameId != game }.values.flatten().map(::DesactivateReward)
    }

    private fun activateFeaturesAssociateWith(game: GameId): List<OutgoingEvent> {
        return gameRewards[game]?.map(::ActivateReward) ?: emptyList()
    }
}