package fr.delphes.feature

import fr.delphes.bot.event.eventHandler.EventHandlers

interface Feature {
    fun registerHandlers(eventHandlers: EventHandlers)

    fun description() : FeatureDescription
}