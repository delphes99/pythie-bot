package fr.delphes.bot.webserver

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.util.pipeline.PipelineContext

suspend fun PipelineContext<Unit, ApplicationCall>.notFound(message: String) {
    context.respond(
        HttpStatusCode.NotFound,
        message
    )
}

suspend fun PipelineContext<Unit, ApplicationCall>.badRequest(message: String) {
    context.respond(
        HttpStatusCode.BadRequest,
        message
    )
}

suspend fun PipelineContext<Unit, ApplicationCall>.ok(message: String) {
    context.respond(
        HttpStatusCode.OK,
        message
    )
}