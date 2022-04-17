package fr.delphes.bot.media

import io.ktor.application.Application
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing

fun Application.MediaModule(
    mediasService: MediasService
) {
    routing {
        static("medias/content") {
            files(mediasService.path())
        }
        get("medias/files") {
            this.context.respond(HttpStatusCode.OK, mediasService.list())
        }
    }
}