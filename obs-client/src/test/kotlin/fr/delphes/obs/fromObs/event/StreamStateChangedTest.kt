package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class StreamStateChangedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/StreamStateChanged.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe StreamStateChanged(
            eventIntent = 64,
            eventData = StreamStateChangedData(
                true,
                StreamStateChangedState.STARTED
            )
        )
    }
})