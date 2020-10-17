package fr.delphes.bot.util.time

import java.time.LocalDateTime

interface Clock {
    fun now(): LocalDateTime
}