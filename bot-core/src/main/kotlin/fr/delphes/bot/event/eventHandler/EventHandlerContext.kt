package fr.delphes.bot.event.eventHandler

typealias EventHandlerContext<T> = suspend EventHandlerParameters<T>.() -> Unit