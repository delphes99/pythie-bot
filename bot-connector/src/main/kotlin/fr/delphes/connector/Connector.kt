package fr.delphes.connector

import io.ktor.application.Application

interface Connector {
    fun endpoints(application: Application)
}