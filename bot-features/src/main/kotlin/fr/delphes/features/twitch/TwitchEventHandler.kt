package fr.delphes.features.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlerAction
import fr.delphes.bot.event.eventHandler.EventHandlerContext
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import kotlin.reflect.KClass

inline fun <reified U : TwitchIncomingEvent> TwitchChannel.handlerFor(noinline action: EventHandlerAction<U>) =
    EventHandler.of<U> {
        if (event.channel == this@handlerFor) {
            action()
        }
    }

fun <U : TwitchIncomingEvent> TwitchChannel.handlersFor(
    clazz: KClass<U>,
    action: EventHandlerAction<U>,
): EventHandlers {
    return EventHandlers.builder().addHandler(clazz) { event: U, bot: Bot ->
        if (event.channel == this) {
            EventHandlerContext(bot, event, bot.featuresManager.stateManager).action()
        }
    }.build()
}

inline fun <reified U : TwitchIncomingEvent> TwitchChannel.handlersFor(noinline action: EventHandlerAction<U>) =
    EventHandlers.of(handlerFor(action))