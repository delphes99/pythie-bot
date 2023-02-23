package fr.delphes.obs.fromObs.event

import fr.delphes.test.serialization.readAndDeserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SceneRemovedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/SceneRemoved.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe SceneRemoved(
            eventIntent = 4,
            eventData = SceneRemovedData(
                isGroup = false,
                sceneName = "main_capture"
            )
        )
    }
})