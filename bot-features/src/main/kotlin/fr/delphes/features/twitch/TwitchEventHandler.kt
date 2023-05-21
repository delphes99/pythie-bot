package fr.delphes.features.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlerContext
import fr.delphes.bot.event.eventHandler.EventHandlerParameters
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import kotlin.reflect.KClass

inline fun <reified U : TwitchIncomingEvent> TwitchChannel.handlerFor(noinline action: EventHandlerContext<U>) =
    EventHandler.of<U> {
        if (event.channel == this@handlerFor) {
            action()
        }
    }

fun <U : TwitchIncomingEvent> TwitchChannel.handlersFor(
    clazz: KClass<U>,
    handler: EventHandlerContext<U>,
): EventHandlers {
    return EventHandlers.builder().addHandler(clazz) { event: U, bot: Bot ->
        if (event.channel == this) {
            EventHandlerParameters(bot, event, bot.featuresManager.stateManager).handler()
        }
    }.build()
}

inline fun <reified U : TwitchIncomingEvent> TwitchChannel.handlersFor(noinline handler: EventHandlerContext<U>) =
    EventHandlers.of(handlerFor(handler))