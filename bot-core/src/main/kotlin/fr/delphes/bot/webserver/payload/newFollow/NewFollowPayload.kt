package fr.delphes.bot.webserver.payload.newFollow

import kotlinx.serialization.Serializable

@Serializable
data class NewFollowPayload(
    val data: List<NewFollowData>
) {
    constructor(vararg datas: NewFollowData) : this(listOf(*datas))
}