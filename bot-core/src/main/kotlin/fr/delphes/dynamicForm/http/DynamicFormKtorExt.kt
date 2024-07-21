package fr.delphes.dynamicForm.http

import fr.delphes.dynamicForm.DynamicFormDTO
import fr.delphes.dynamicForm.DynamicFormDTOForSerialization
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.util.pipeline.PipelineContext
import kotlinx.serialization.json.Json

inline suspend fun <reified T> PipelineContext<Unit, ApplicationCall>.getDynamicForm(serializer: Json): T {
    //TODO why could we not use the receive with serializer defined in the ContentNegotiation
    val payload = this.call.receive<String>()
    val dto = serializer.decodeFromString<DynamicFormDTOForSerialization>(payload) as DynamicFormDTO<*>
    val form = dto.build()

    if (form !is T) {
        throw IllegalArgumentException("Invalid form type")
    }

    return form
}