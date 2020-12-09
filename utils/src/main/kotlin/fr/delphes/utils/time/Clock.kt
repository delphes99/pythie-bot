package fr.delphes.utils.time

import java.time.LocalDateTime

interface Clock {
    fun now(): LocalDateTime
}