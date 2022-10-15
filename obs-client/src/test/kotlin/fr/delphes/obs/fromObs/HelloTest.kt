package fr.delphes.obs.fromObs

import fr.delphes.obs.deserialize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class HelloTest {
    @Test
    internal fun `deserialize (with authentication required)`() {
        val payload = "/obs/fromObs/Hello_with_authentication.json".deserialize<FromOBSMessagePayload>()

        payload shouldBe Hello(
            HelloPayloadData(
                obsWebSocketVersion = "5.0.1",
                rpcVersion = 1,
                authentication = Authentication(
                    challenge = "+IxH4CnCiqpX1rM9scsNynZzbOe4KhDeYcTNS3PDaeY=",
                    salt = "lM1GncleQOaCu9lT1yeUZhFYnqhsLLP1G5lAGo3ixaI="
                )
            )
        )
    }

    @Test
    internal fun `deserialize (with no authentication required)`() {
        val payload = "/obs/fromObs/Hello_without_authentication.json".deserialize<FromOBSMessagePayload>()

        payload shouldBe Hello(
            HelloPayloadData(
                obsWebSocketVersion = "5.0.1",
                rpcVersion = 1
            )
        )
    }
}