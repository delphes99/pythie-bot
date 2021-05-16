package fr.delphes.utils.time

import java.time.Duration

fun Duration.prettyPrint(): String {
    if(this.isZero) {
        return "0s"
    }
    return (this.toString()
        .substring(2)
        .replace("(\\d[HMS])(?!$)", "$1 ")
        .split('.')[0] + 's')
        .lowercase()
}

fun secondsOf(seconds: Int): Duration = Duration.ofSeconds(seconds.toLong())