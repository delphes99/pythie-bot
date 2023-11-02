package fr.delphes.features.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlerContext
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.eventHandler.IncomingEventHandlerAction
import fr.delphes.bot.event.incoming.IncomingEventWrapper
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import kotlin.reflect.KClass

inline fun <reified U : TwitchIncomingEvent> TwitchChannel.handlerFor(noinline action: IncomingEventHandlerAction<U>) =
    EventHandler.of<U> {
        if (event.data.channel == this@handlerFor) {
            action()
        }
    }

fun <U : TwitchIncomingEvent> TwitchChannel.handlersFor(
    clazz: KClass<U>,
    action: IncomingEventHandlerAction<U>,
): EventHandlers {
    return EventHandlers.builder().addHandler(clazz) { event: IncomingEventWrapper<U>, bot: Bot ->
        if (event.data.channel == this) {
            EventHandlerContext(bot, event, bot.stateManager).action()
        }
    }.build()
}

inline fun <reified U : TwitchIncomingEvent> TwitchChannel.handlersFor(noinline action: IncomingEventHandlerAction<U>) =
    EventHandlers.of(handlerFor(action))