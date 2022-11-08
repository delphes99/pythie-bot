package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class ExitStartedTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/event/ExitStarted.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe ExitStarted(
            eventIntent = 1
        )
    }
}