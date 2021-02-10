package fr.delphes.twitch.clip

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.clips.Clip

data class ClipCreated(
    val channel: TwitchChannel,
    val clip: Clip
)