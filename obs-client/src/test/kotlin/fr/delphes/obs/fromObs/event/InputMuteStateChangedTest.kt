package fr.delphes.obs.fromObs.event

import fr.delphes.test.serialization.readAndDeserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class InputMuteStateChangedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/InputMuteStateChanged.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe InputMuteStateChanged(
            eventIntent = 8,
            eventData = InputMuteStateChangedData(
                inputName = "Audio du Bureau",
                inputMuted = true
            )
        )
    }
})