package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SceneTransitionVideoEndedTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/event/SceneTransitionVideoEnded.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe SceneTransitionVideoEnded(
            eventIntent = 16,
            eventData = SceneTransitionVideoEndedData(
                transitionName = "Fondu"
            )
        )
    }
}