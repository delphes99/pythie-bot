package fr.delphes.dynamicForm.http

import fr.delphes.dynamicForm.DynamicFormDTO
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.util.pipeline.PipelineContext
import kotlinx.serialization.Contextual

inline suspend fun <reified T> PipelineContext<Unit, ApplicationCall>.getDynamicForm(): T {
    val dto = this.call.receive<DynamicFormDTO<@Contextual Any>>()
    val form = dto.build()

    if (form !is T) {
        throw IllegalArgumentException("Invalid form type")
    }

    return form
}