package fr.delphes.obs

import fr.delphes.obs.event.SwitchScenes

class ObsListener(
    val onSwitchScene: (SwitchScenes) -> Unit = {},
    val onError: () -> Unit = {},
)