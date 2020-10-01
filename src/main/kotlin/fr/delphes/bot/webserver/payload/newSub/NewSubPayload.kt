package fr.delphes.bot.webserver.payload.newSub

import kotlinx.serialization.Serializable

@Serializable
data class NewSubPayload(
    val data: List<NewSubData>
) {
    constructor(vararg datas: NewSubData) : this(listOf(*datas))
}