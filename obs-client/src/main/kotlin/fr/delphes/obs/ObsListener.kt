package fr.delphes.obs

import fr.delphes.obs.event.SourceFilterVisibilityChanged
import fr.delphes.obs.event.SwitchScenes

class ObsListener(
    val onSwitchScene: suspend (SwitchScenes) -> Unit = {},
    val onSourceFilterVisibilityChanged: suspend (SourceFilterVisibilityChanged) -> Unit = {},
    val onError: suspend (message: String) -> Unit = {},
)