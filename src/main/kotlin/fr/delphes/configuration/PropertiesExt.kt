package fr.delphes.configuration

import java.util.Properties

private object PropertiesLoad

fun loadProperties(path: String): Properties {
    val properties = Properties()
    properties.load(PropertiesLoad.javaClass.classLoader.getResourceAsStream(path))
    return properties
}