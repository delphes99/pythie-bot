package fr.delphes.obs.fromObs.requestResponse

import fr.delphes.test.serialization.readAndDeserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import fr.delphes.obs.fromObs.RequestStatus
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class GetSceneItemListTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/requestResponse/GetSceneItemList.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe GetSceneItemList(
            requestId = "ed29f881-6fc5-4e4e-ac4d-c14102ec941f",
            requestStatus = RequestStatus(
                result = true,
                code = 100
            ),
            responseData = GetSceneItemListData(
                SceneItem("main_capture", 7),
                SceneItem("Streamlabs alert", 2),
                SceneItem("Streamlabs emote wall", 3),
                SceneItem("Discord", 6),
                SceneItem("webcam", 8),
                SceneItem("Overlay", 4)
            )
        )
    }
})