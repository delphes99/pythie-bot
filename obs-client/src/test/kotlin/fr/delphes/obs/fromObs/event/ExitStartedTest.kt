package fr.delphes.obs.fromObs.event

import fr.delphes.test.serialization.readAndDeserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class ExitStartedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/ExitStarted.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe ExitStarted(
            eventIntent = 1
        )
    }
})