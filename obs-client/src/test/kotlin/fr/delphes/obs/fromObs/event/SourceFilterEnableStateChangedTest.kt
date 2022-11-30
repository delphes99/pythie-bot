package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SourceFilterEnableStateChangedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/SourceFilterEnableStateChanged.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe SourceFilterEnableStateChanged(
            eventIntent = 32,
            eventData = SourceFilterEnableStateChangedData(
                filterEnabled = false,
                filterName = "matrix",
                sourceName = "webcam",
            )
        )
    }
})