package fr.delphes.obs.fromObs.event

import fr.delphes.test.serialization.readAndDeserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SceneItemSelectedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/SceneItemSelected.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe SceneItemSelected(
            eventIntent = 128,
            eventData = SceneItemSelectedData(
                sceneItemId = 4,
                sceneName = "in_game",
            )
        )
    }
})