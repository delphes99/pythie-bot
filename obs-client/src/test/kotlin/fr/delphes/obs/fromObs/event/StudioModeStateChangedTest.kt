package fr.delphes.obs.fromObs.event

import fr.delphes.obs.fromObs.FromOBSMessagePayload
import fr.delphes.test.serialization.readAndDeserialize
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class StudioModeStateChangedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/StudioModeStateChanged.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe StudioModeStateChanged(
            eventIntent = 1024,
            eventData = StudioModeStateChangedData(
                studioModeEnabled = true,
            )
        )
    }
})