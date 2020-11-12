package fr.delphes.bot.webserver.payload.streamInfos

import kotlinx.serialization.Serializable

@Serializable
data class StreamInfosPayload(
    val data: List<StreamInfosData> = emptyList()
) {
    constructor(vararg datas: StreamInfosData) : this(listOf(*datas))
}