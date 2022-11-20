package fr.delphes.rework.feature

import fr.delphes.bot.Bot
import fr.delphes.bot.event.incoming.IncomingEvent

interface FeatureRuntime {
    val id: FeatureId
    suspend fun handleIncomingEvent(event: IncomingEvent, bot: Bot)
}