package fr.delphes.obs.fromObs.requestResponse

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import fr.delphes.obs.fromObs.RequestStatus
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SetSceneItemEnabledTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/requestResponse/SetSceneItemEnabled.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe SetSceneItemEnabled(
            "f4384303-5a4d-4891-b573-88ee011fd959",
            RequestStatus(true, 100)
        )
    }
}