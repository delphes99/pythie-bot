package fr.delphes.bot.event.eventHandler

typealias EventHandlerAction<T> = suspend EventHandlerContext<T>.() -> Unit