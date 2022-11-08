package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SceneRemovedTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/event/SceneRemoved.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe SceneRemoved(
            eventIntent = 4,
            eventData = SceneRemovedData(
                isGroup = false,
                sceneName = "main_capture"
            )
        )
    }
}