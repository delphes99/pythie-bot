package fr.delphes.twitch.api.vip.payload

import fr.delphes.test.serialization.readAndDeserialize
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class GetVIPsPayloadTest : ShouldSpec({
    should("deserialize") {

        "/twitch/api/helix/vip/payload.json".readAndDeserialize<GetVIPsPayload>() shouldBe
                GetVIPsPayload(
                    VIPPayload(
                        "11111",
                        "UserDisplayName1",
                        "userloginname1"
                    ),
                    VIPPayload(
                        "22222",
                        "UserDisplayName2",
                        "userloginname2"
                    )
                )
    }
})