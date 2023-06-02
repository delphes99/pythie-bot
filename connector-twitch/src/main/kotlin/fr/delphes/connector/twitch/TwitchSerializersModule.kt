package fr.delphes.connector.twitch

import fr.delphes.bot.connector.statistics.StatisticEventData
import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.connector.twitch.outgoingEvent.ActivateReward
import fr.delphes.connector.twitch.outgoingEvent.DeactivateReward
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.connector.twitch.statistics.event.CommandAskTwitchStatistic
import fr.delphes.connector.twitch.statistics.event.MessageReceivedTwitchStatistic
import fr.delphes.connector.twitch.statistics.event.UserFollowTwitchStatistic
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val twitchSerializersModule = SerializersModule {
    polymorphic(OutgoingEventBuilder::class) {
        subclass(SendMessage.Companion.Builder::class)
        subclass(ActivateReward.Companion.Builder::class)
        subclass(DeactivateReward.Companion.Builder::class)
    }

    polymorphic(StatisticEventData::class) {
        subclass(MessageReceivedTwitchStatistic::class)
        subclass(UserFollowTwitchStatistic::class)
        subclass(CommandAskTwitchStatistic::class)
    }
}