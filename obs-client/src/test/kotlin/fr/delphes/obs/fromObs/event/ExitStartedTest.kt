package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class ExitStartedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/ExitStarted.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe ExitStarted(
            eventIntent = 1
        )
    }
})