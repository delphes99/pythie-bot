package fr.delphes.obs.fromObs.requestResponse

import fr.delphes.test.serialization.readAndDeserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import fr.delphes.obs.fromObs.RequestStatus
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SetSceneItemTransformTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/requestResponse/SetSceneItemTransform.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe SetSceneItemTransform(
            requestId = "e3502461-e03c-422c-9dc9-7c23690f3ed7",
            requestStatus = RequestStatus(
                result = true,
                code = 100
            )
        )
    }
})