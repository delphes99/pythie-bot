package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class InputMuteStateChangedTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/event/InputMuteStateChanged.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe InputMuteStateChanged(
            eventIntent = 8,
            eventData = InputMuteStateChangedData(
                inputName = "Audio du Bureau",
                inputMuted = true
            )
        )
    }
}