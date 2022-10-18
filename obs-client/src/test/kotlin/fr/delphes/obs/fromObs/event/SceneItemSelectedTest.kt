package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SceneItemSelectedTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/event/SceneItemSelected.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe SceneItemSelected(
            eventIntent = 128,
            eventData = SceneItemSelectedData(
                sceneItemId = 4,
                sceneName = "in_game",
            )
        )
    }
}