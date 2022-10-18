package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SceneItemEnableStateChangedTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/event/SceneItemEnableStateChanged.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe SceneItemEnableStateChanged(
            eventIntent = 128,
            eventData = SceneItemEnableStateChangedData(
                sceneItemEnabled = true,
                sceneItemId = 4,
                sceneName = "in_game"
            )
        )
    }
}
