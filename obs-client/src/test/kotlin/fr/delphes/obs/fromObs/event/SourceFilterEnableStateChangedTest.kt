package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SourceFilterEnableStateChangedTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/event/SourceFilterEnableStateChanged.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe SourceFilterEnableStateChanged(
            32,
            SourceFilterEnableStateChangedData(
                false,
                "matrix",
                "webcam",
            )
        )
    }
}