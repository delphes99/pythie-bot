package fr.delphes.obs

import fr.delphes.obs.fromObs.event.SourceFilterEnableStateChanged
import fr.delphes.obs.fromObs.event.CurrentProgramSceneChanged

class ObsListener(
    val onSwitchScene: suspend (CurrentProgramSceneChanged) -> Unit = {},
    val onSourceFilterEnableStateChanged: suspend (SourceFilterEnableStateChanged) -> Unit = {},
    val onError: suspend (message: String) -> Unit = {},
    val onExit: suspend () -> Unit = {},
)