package fr.delphes.time

import java.time.LocalDateTime

interface Clock {
    fun now(): LocalDateTime
}