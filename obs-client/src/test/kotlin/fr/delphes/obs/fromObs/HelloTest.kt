package fr.delphes.obs.fromObs

import fr.delphes.obs.deserialize
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class HelloTest : ShouldSpec({
    should("deserialize (with authentication required)") {
        val payload = "/obs/fromObs/Hello_with_authentication.json".deserialize<FromOBSMessagePayload>()

        payload shouldBe Hello(
            d = HelloPayloadData(
                obsWebSocketVersion = "5.0.1",
                rpcVersion = 1,
                authentication = Authentication(
                    challenge = "+IxH4CnCiqpX1rM9scsNynZzbOe4KhDeYcTNS3PDaeY=",
                    salt = "lM1GncleQOaCu9lT1yeUZhFYnqhsLLP1G5lAGo3ixaI="
                )
            )
        )
    }

    should("deserialize (with no authentication required)") {
        val payload = "/obs/fromObs/Hello_without_authentication.json".deserialize<FromOBSMessagePayload>()

        payload shouldBe Hello(
            HelloPayloadData(
                obsWebSocketVersion = "5.0.1",
                rpcVersion = 1
            )
        )
    }
})