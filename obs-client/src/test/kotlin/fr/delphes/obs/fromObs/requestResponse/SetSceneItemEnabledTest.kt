package fr.delphes.obs.fromObs.requestResponse

import fr.delphes.test.serialization.readAndDeserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import fr.delphes.obs.fromObs.RequestStatus
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SetSceneItemEnabledTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/requestResponse/SetSceneItemEnabled.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe SetSceneItemEnabled(
            requestId = "f4384303-5a4d-4891-b573-88ee011fd959",
            requestStatus = RequestStatus(
                result = true,
                code = 100
            )
        )
    }
})