package fr.delphes.features

import fr.delphes.bot.event.eventHandler.EventHandlerAction
import fr.delphes.bot.event.eventHandler.EventHandlerContext
import fr.delphes.bot.event.incoming.IncomingEventWrapper
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe

typealias TestIncomingEventHandlerAction<T> = TestEventHandlerAction<IncomingEventWrapper<T>>

class TestEventHandlerAction<T> : EventHandlerAction<T> {
    private var hasBeenCalled = 0
    override suspend fun invoke(context: EventHandlerContext<T>) {
        hasBeenCalled++
    }

    fun shouldHaveBeenCalled() = hasBeenCalled shouldBeGreaterThan 0

    fun shouldHaveBeenCalled(times: Int) = hasBeenCalled shouldBe times

    fun shouldNotHaveBeenCalled() = hasBeenCalled shouldBe 0
}