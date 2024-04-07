package fr.delphes.dynamicForm

import fr.delphes.bot.Bot
import fr.delphes.bot.webserver.badRequest
import fr.delphes.bot.webserver.notFound
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.DynamicFormAdminModule(bot: Bot) {
    routing {
        get("dynamicForms/forms") {
            this.context.respond(HttpStatusCode.OK, bot.dynamicFormRegistry.entries.map { it.name })
        }
        get("dynamicForms/form/{name}") {
            val name = this.context.parameters["name"] ?: return@get badRequest("Name is required")

            val form = bot.dynamicFormRegistry.find(name) ?: return@get notFound("Form not found")
            //TODO return empty form description
            this.context.respond(HttpStatusCode.OK, form.name)
        }
        get("dynamicForms/tag/{tag}") {
            val tag = this.context.parameters["tag"] ?: return@get badRequest("Tag is required")

            //TODO return empty form description
            this.context.respond(HttpStatusCode.OK, bot.dynamicFormRegistry.findByTag(tag).map { it.name })
        }

    }
}