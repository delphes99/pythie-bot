package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.event.incoming.IncomingEventWrapper

typealias EventHandlerAction<T> = suspend EventHandlerContext<T>.() -> Unit

typealias IncomingEventHandlerAction<T> = suspend EventHandlerContext<IncomingEventWrapper<T>>.() -> Unit