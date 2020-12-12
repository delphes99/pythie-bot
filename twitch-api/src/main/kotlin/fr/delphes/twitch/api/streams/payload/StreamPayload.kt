package fr.delphes.twitch.api.streams.payload

import kotlinx.serialization.Serializable

@Serializable
data class StreamPayload(
     val data: List<StreamInfos>
) {
     constructor(vararg datas: StreamInfos) : this(listOf(*datas))
}