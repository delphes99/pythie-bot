package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class InputRemovedTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/event/InputRemoved.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe InputRemoved(
            eventIntent = 8,
            eventData = InputRemovedData(
                inputName = "fullscreen"
            )
        )
    }
}