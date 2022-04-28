package fr.delphes.bot.media

import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.readAllParts
import io.ktor.http.content.streamProvider
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.http.content.files
import io.ktor.server.http.content.static
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

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
        post("medias/upload") { _ ->
            val multipart = call.receiveMultipart()

            val parts = multipart.readAllParts()

            val fileName = parts.filterIsInstance<PartData.FormItem>().firstOrNull { it.name == "filename" }?.value
            if (fileName == null) {
                this.context.respond(HttpStatusCode.BadRequest, "Missing filename")
                return@post
            }
            val file = parts.filterIsInstance<PartData.FileItem>().firstOrNull { it.name == "file" }?.streamProvider?.invoke()?.readBytes()
            if (file == null) {
                this.context.respond(HttpStatusCode.BadRequest, "Missing file")
                return@post
            }

            mediasService.upload(fileName, file)
            this.context.respond(HttpStatusCode.OK)
        }
    }
}