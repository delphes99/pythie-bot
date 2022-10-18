package fr.delphes.obs.fromObs.event

import fr.delphes.obs.deserialize
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class InputAudioTracksChangedTest {
    @Test
    internal fun deserialize() {
        val payload = "/obs/fromObs/event/InputAudioTracksChanged.json".deserialize<FromOBSMessagePayload>()

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
}