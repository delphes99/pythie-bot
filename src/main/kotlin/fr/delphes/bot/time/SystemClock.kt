package fr.delphes.bot.time

import java.time.LocalDateTime

object SystemClock : Clock {
    override fun now(): LocalDateTime = LocalDateTime.now()
}