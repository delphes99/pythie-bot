package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SceneTransitionEndedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/SceneTransitionEnded.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe SceneTransitionEnded(
            eventIntent = 16,
            eventData = SceneTransitionEndedData(
                transitionName = "Fondu"
            )
        )
    }
})