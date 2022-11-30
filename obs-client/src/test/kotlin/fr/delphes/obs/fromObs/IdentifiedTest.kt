package fr.delphes.obs.fromObs

import fr.delphes.obs.deserialize
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class IdentifiedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/Identified.json".deserialize<FromOBSMessagePayload>()

        payload shouldBe Identified(
            d = IdentifiedData(
                negotiatedRpcVersion = 1
            )
        )
    }
})