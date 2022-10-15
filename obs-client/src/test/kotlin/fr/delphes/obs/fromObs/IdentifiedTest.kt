package fr.delphes.obs.fromObs

import fr.delphes.obs.deserialize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class IdentifiedTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/Identified.json".deserialize<FromOBSMessagePayload>()

        payload shouldBe Identified(
            IdentifiedData(
                1
            )
        )
    }

}