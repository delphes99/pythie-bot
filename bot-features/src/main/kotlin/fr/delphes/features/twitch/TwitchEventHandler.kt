package fr.delphes.features.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlerContext
import fr.delphes.bot.event.eventHandler.EventHandlerParameters
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import kotlin.reflect.KClass

fun <U : TwitchIncomingEvent> TwitchChannel.handlerFor(
    clazz: KClass<U>,
    handler: EventHandlerContext<U>,
): EventHandlers {
    return EventHandlers.builder().addHandler(clazz) { event: U, bot: Bot ->
        if (event.channel == this) {
            EventHandlerParameters(bot, event, bot.featuresManager.stateManager).handler()
        }
    }.build()
}

inline fun <reified U : TwitchIncomingEvent> TwitchChannel.handlerFor(noinline handler: EventHandlerContext<U>) =
    handlerFor(U::class, handler)