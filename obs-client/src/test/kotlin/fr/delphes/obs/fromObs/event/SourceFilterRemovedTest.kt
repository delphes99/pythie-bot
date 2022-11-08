package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SourceFilterRemovedTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/event/SourceFilterRemoved.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe SourceFilterRemoved(
            eventIntent = 32,
            eventData = SourceFilterRemovedData(
                filterName = "Background Removal",
                sourceName = "Webcam",
            )
        )
    }
}