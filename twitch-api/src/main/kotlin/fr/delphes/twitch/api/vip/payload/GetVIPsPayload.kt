package fr.delphes.twitch.api.vip.payload

import kotlinx.serialization.Serializable

@Serializable
data class GetVIPsPayload(val data: List<VIPPayload>) {
    constructor(vararg vips: VIPPayload) : this(vips.toList())
}