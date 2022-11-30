package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SourceFilterRemovedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/SourceFilterRemoved.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe SourceFilterRemoved(
            eventIntent = 32,
            eventData = SourceFilterRemovedData(
                filterName = "Background Removal",
                sourceName = "Webcam",
            )
        )
    }
})