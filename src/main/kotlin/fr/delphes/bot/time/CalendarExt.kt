package fr.delphes.bot.time

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.TimeZone

fun Calendar.toLocalDateTime(): LocalDateTime {
    val zid = timeZone?.let(TimeZone::toZoneId) ?: ZoneId.systemDefault()
    return LocalDateTime.ofInstant(toInstant(), zid)
}