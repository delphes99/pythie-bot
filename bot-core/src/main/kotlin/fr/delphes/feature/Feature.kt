package fr.delphes.feature

import fr.delphes.bot.event.eventHandler.EventHandlers

interface Feature<DESC : FeatureDescription> {
    fun registerHandlers(eventHandlers: EventHandlers)

    fun description() : DESC
}