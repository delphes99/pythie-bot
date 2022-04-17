package fr.delphes.bot.media

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.files
import io.ktor.http.content.readAllParts
import io.ktor.http.content.static
import io.ktor.http.content.streamProvider
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
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
        post("medias/upload") { _ ->
            val multipart = call.receiveMultipart()

            val parts = multipart.readAllParts()

            val fileName = parts.filterIsInstance<PartData.FormItem>().firstOrNull { it.name == "name" }?.value
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