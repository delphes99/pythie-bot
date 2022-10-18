package fr.delphes.obs.fromObs.requestResponse

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import fr.delphes.obs.fromObs.RequestStatus
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SetSourceFilterEnabledTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/requestResponse/SetSourceFilterEnabled.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe SetSourceFilterEnabled(
            requestId = "e0856478-0eb3-4402-9d80-112bfef90d62",
            requestStatus = RequestStatus(
                result = true,
                code = 100
            )
        )
    }
}