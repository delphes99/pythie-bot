package fr.delphes.obs.fromObs.event

import fr.delphes.test.serialization.readAndDeserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SceneItemEnableStateChangedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/SceneItemEnableStateChanged.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe SceneItemEnableStateChanged(
            eventIntent = 128,
            eventData = SceneItemEnableStateChangedData(
                sceneItemEnabled = true,
                sceneItemId = 4,
                sceneName = "in_game"
            )
        )
    }
})