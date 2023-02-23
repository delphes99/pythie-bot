package fr.delphes.obs.fromObs

import fr.delphes.test.serialization.readAndDeserialize
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class IdentifiedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/Identified.json".readAndDeserialize<FromOBSMessagePayload>()

        payload shouldBe Identified(
            d = IdentifiedData(
                negotiatedRpcVersion = 1
            )
        )
    }
})