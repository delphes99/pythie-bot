package fr.delphes.features.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import kotlin.reflect.KClass

fun <U : TwitchIncomingEvent> TwitchChannel.handlerFor(clazz: KClass<U>, handler: suspend TwitchEventParameters<U>.() -> Unit): EventHandlers {
    return EventHandlers.builder().addHandler(clazz) { event: U, bot: Bot ->
        if (event.channel == this) {
            TwitchEventParameters(bot, event).handler()
        }
    }.build()
}

inline fun <reified U : TwitchIncomingEvent> TwitchChannel.handlerFor(noinline handler: suspend TwitchEventParameters<U>.() -> Unit) = handlerFor(U::class, handler)