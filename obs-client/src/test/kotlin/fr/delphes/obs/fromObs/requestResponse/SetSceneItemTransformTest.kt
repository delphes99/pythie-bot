package fr.delphes.obs.fromObs.requestResponse

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import fr.delphes.obs.fromObs.RequestStatus
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SetSceneItemTransformTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/requestResponse/SetSceneItemTransform.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe SetSceneItemTransform(
            "e3502461-e03c-422c-9dc9-7c23690f3ed7",
            RequestStatus(true, 100)
        )
    }
}