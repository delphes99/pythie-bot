package fr.delphes.obs.fromObs.event

import kotlinx.serialization.Serializable

@Serializable
data class CurrentProgramSceneChanged(
    val sceneName: String
) : Event