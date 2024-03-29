package fr.delphes.obs.fromObs.event

import fr.delphes.test.serialization.readAndDeserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SceneTransitionVideoEndedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/SceneTransitionVideoEnded.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe SceneTransitionVideoEnded(
            eventIntent = 16,
            eventData = SceneTransitionVideoEndedData(
                transitionName = "Fondu"
            )
        )
    }
})