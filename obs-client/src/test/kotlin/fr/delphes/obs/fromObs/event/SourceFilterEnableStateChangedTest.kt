package fr.delphes.obs.fromObs.event

import fr.delphes.test.serialization.readAndDeserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SourceFilterEnableStateChangedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/SourceFilterEnableStateChanged.json".readAndDeserialize<FromOBSMessagePayload>()

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