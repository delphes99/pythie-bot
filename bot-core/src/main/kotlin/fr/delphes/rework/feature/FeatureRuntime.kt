package fr.delphes.rework.feature

import fr.delphes.bot.Bot
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.IncomingEventWrapper

interface FeatureRuntime {
    val id: FeatureId
    suspend fun handleIncomingEvent(event: IncomingEventWrapper<out IncomingEvent>, bot: Bot)
}