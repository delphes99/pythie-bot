package fr.delphes.obs.fromObs.event

import fr.delphes.test.serialization.readAndDeserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class InputAudioTracksChangedTest : ShouldSpec({
    should("deserialize") {
        val payload = "/obs/fromObs/event/InputAudioTracksChanged.json".readAndDeserialize<FromOBSMessagePayload>()

        payload.d shouldBe InputAudioTracksChanged(
            eventIntent = 8,
            eventData = InputAudioTracksChangedData(
                inputName = "Audio du Bureau",
                inputAudioTracks = InputAudioTracks(
                    track1 = true,
                    track2 = true,
                    track3 = false,
                    track4 = true,
                    track5 = true,
                    track6 = false,
                )
            )
        )
    }
})