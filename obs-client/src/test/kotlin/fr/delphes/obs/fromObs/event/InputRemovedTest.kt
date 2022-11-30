package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class InputRemovedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/InputRemoved.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe InputRemoved(
            eventIntent = 8,
            eventData = InputRemovedData(
                inputName = "fullscreen"
            )
        )
    }
})