package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class StreamStateChangedTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/event/StreamStateChanged.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe StreamStateChanged(
            eventIntent = 64,
            eventData = StreamStateChangedData(
                true,
                StreamStateChangedState.STARTED
            )
        )
    }
}