package fr.delphes.generation

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment

object GenerationUtils {
    fun getModuleName(environment: SymbolProcessorEnvironment): String {
        return environment.options["module-name"] ?: "default"
    }
}