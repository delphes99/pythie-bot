package fr.delphes.bot.time

import java.time.LocalDateTime

interface Clock {
    fun now(): LocalDateTime
}