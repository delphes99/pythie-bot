package fr.delphes.obs.fromObs.requestResponse

import fr.delphes.test.serialization.readAndDeserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import fr.delphes.obs.fromObs.RequestStatus
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SetSourceFilterEnabledTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/requestResponse/SetSourceFilterEnabled.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe SetSourceFilterEnabled(
            requestId = "e0856478-0eb3-4402-9d80-112bfef90d62",
            requestStatus = RequestStatus(
                result = true,
                code = 100
            )
        )
    }
})