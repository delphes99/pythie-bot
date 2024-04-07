package fr.delphes.generation.utils

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import kotlin.reflect.KClass

object GenerationUtils {
    fun baseGeneratedPackage(moduleName: String): String {
        return "fr.delphes.$moduleName.generated"
    }

    fun getModuleName(environment: SymbolProcessorEnvironment): String {
        return environment.options["module-name"] ?: "default"
    }

    fun Resolver.process(
        logger: KSPLogger,
        kClass: KClass<out Any>,
        processClasses: (List<KSClassDeclaration>) -> Unit,
    ): List<KSAnnotated> {
        val (valids, invalids) =
            getSymbolsWithAnnotation(kClass.java.name)
                .partition { it.validate() }

        if (valids.isNotEmpty()) {
            logger.info("Generate entry for ${valids.size} classes")
            processClasses(valids.filterIsInstance<KSClassDeclaration>())
        } else {
            logger.info("No class to generate")
        }

        return invalids
    }

    fun Resolver.processEach(
        logger: KSPLogger,
        kClass: KClass<out Any>,
        processClasses: (KSClassDeclaration) -> Unit,
    ): List<KSAnnotated> {
        return process(logger, kClass) { classes ->
            classes.forEach(processClasses)
        }
    }
}