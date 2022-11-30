package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class CurrentProgramSceneChangedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/CurrentProgramSceneChanged.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe CurrentProgramSceneChanged(
            eventIntent = 4,
            eventData = CurrentProgramSceneChangedData(
                sceneName = "in_game"
            )
        )
    }
})