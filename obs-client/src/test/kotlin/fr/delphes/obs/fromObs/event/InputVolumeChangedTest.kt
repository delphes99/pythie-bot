package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class InputVolumeChangedTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/event/InputVolumeChanged.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe InputVolumeChanged(
            eventIntent = 8,
            eventData = InputVolumeChangedData(
                inputName = "Overlay",
                inputVolumeDb = 0.0,
                inputVolumeMul = 1.0
            )
        )
    }
}