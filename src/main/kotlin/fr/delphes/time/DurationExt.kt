package fr.delphes.time

import java.time.Duration

fun Duration.prettyPrint(): String {
    return (this.toString()
        .substring(2)
        .replace("(\\d[HMS])(?!$)", "$1 ")
        .split('.')[0] + 's')
        .toLowerCase()
}