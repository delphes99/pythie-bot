package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class CurrentProgramSceneChangedTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/event/CurrentProgramSceneChanged.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe CurrentProgramSceneChanged(
            4,
            CurrentProgramSceneChangedData(
                "in_game"
            )
        )
    }
}