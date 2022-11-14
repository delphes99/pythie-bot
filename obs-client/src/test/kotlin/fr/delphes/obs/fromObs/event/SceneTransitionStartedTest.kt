package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SceneTransitionStartedTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/event/SceneTransitionStarted.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe SceneTransitionStarted(
            eventIntent = 16,
            eventData = SceneTransitionStartedData(
                transitionName = "Fondu"
            )
        )
    }
}