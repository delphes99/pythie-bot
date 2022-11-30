package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class CurrentPreviewSceneChangedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/CurrentPreviewSceneChanged.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe CurrentPreviewSceneChanged(
            eventIntent = 4,
            eventData = CurrentPreviewSceneChangedData(
                sceneName = "start_screen"
            )
        )
    }
})