package fr.delphes.obs.fromObs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("GetSceneItemList")
data class GetSceneItemList(
    override val requestId: String,
    override val requestStatus: RequestStatus,
    override val responseData: GetSceneItemListData,
): RequestResponseData()

@Serializable
data class GetSceneItemListData(
    val sceneItems: List<SceneItem>
) {
    constructor(vararg sceneItems: SceneItem): this(listOf(*sceneItems))
}

@Serializable
data class SceneItem(
    val sourceName: String,
    val sceneItemId: Long,
)
