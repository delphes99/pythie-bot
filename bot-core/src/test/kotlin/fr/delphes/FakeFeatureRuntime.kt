package fr.delphes

import fr.delphes.bot.Bot
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime

data class FakeFeatureRuntime(
    override val id: FeatureId = FeatureId("id")
) : FeatureRuntime {
    var isHandleIncomingEventCalled = false
    override suspend fun handleIncomingEvent(event: IncomingEvent, bot: Bot) {
        isHandleIncomingEventCalled = true
    }
}