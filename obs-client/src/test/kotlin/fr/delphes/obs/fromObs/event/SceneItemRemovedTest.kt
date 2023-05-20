package fr.delphes.obs.fromObs.event

import fr.delphes.obs.fromObs.FromOBSMessagePayload
import fr.delphes.test.serialization.readAndDeserialize
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SceneItemRemovedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/SceneItemRemoved.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe SceneItemRemoved(
            eventIntent = 128,
            eventData = SceneItemRemovedData(
                sceneItemId = 17,
                sceneName = "in_game",
                sourceName = "newItem",
            )
        )
    }
})