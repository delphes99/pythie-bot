package fr.delphes.obs.fromObs

import fr.delphes.obs.deserialize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class GetSceneItemListTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/requestResponse/GetSceneItemList.json".deserialize<FromOBSMessagePayload>()

        payload.d.shouldBeInstanceOf<RequestResponse>()
        (payload.d as RequestResponse).d shouldBe GetSceneItemList(
            "560f6959-2040-43cd-8e35-961bf0e354df",
            RequestStatus(true, 100),
            GetSceneItemListData(
                SceneItem("main_capture"),
                SceneItem("Streamlabs alert"),
                SceneItem("Streamlabs emote wall"),
                SceneItem("Discord"),
                SceneItem("webcam"),
                SceneItem("Overlay")
            )
        )
    }
}