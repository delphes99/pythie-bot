package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SceneTransitionEndedTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/event/SceneTransitionEnded.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe SceneTransitionEnded(
            eventIntent = 16,
            eventData = SceneTransitionEndedData(
                transitionName = "Fondu"
            )
        )
    }
}