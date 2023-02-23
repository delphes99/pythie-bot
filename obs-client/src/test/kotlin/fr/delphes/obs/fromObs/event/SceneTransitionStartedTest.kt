package fr.delphes.obs.fromObs.event

import fr.delphes.test.serialization.readAndDeserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SceneTransitionStartedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/SceneTransitionStarted.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe SceneTransitionStarted(
            eventIntent = 16,
            eventData = SceneTransitionStartedData(
                transitionName = "Fondu"
            )
        )
    }
})