package fr.delphes.obs.fromObs.event

import fr.delphes.obs.fromObs.FromOBSMessagePayload
import fr.delphes.test.serialization.readAndDeserialize
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SceneItemCreatedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/SceneItemCreated.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe SceneItemCreated(
            eventIntent = 128,
            eventData = SceneItemCreatedData(
                sceneItemId = 17,
                sceneItemIndex = 6,
                sceneName = "in_game",
                sourceName = "newItem",
            )
        )
    }
})