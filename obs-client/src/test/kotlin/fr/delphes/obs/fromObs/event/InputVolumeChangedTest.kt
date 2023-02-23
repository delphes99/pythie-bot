package fr.delphes.obs.fromObs.event

import fr.delphes.test.serialization.readAndDeserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class InputVolumeChangedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/InputVolumeChanged.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe InputVolumeChanged(
            eventIntent = 8,
            eventData = InputVolumeChangedData(
                inputName = "Overlay",
                inputVolumeDb = 0.0,
                inputVolumeMul = 1.0
            )
        )
    }
})