package fr.delphes.obs.fromObs.event

import fr.delphes.test.serialization.readAndDeserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SceneTransitionEndedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/SceneTransitionEnded.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe SceneTransitionEnded(
            eventIntent = 16,
            eventData = SceneTransitionEndedData(
                transitionName = "Fondu"
            )
        )
    }
})