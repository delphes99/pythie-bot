package fr.delphes.obs.fromObs.requestResponse

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import fr.delphes.obs.fromObs.RequestStatus
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class GetSceneItemListTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/requestResponse/GetSceneItemList.json".deserialize<FromOBSMessagePayload>()

        payload.d shouldBe GetSceneItemList(
            "ed29f881-6fc5-4e4e-ac4d-c14102ec941f",
            RequestStatus(true, 100),
            GetSceneItemListData(
                SceneItem("main_capture", 7),
                SceneItem("Streamlabs alert", 2),
                SceneItem("Streamlabs emote wall", 3),
                SceneItem("Discord", 6),
                SceneItem("webcam", 8),
                SceneItem("Overlay", 4)
            )
        )
    }
}