package fr.delphes.generation

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspArgs
import com.tschuchort.compiletesting.kspWithCompilation
import com.tschuchort.compiletesting.symbolProcessorProviders

fun String.shouldCompileWithProvider(
    provider: SymbolProcessorProvider,
    assertion: KotlinCompilation.Result.() -> Unit,
) {
    val source = SourceFile.kotlin(
        "MyEvent.kt", this
    )
    KotlinCompilation()
        .apply {
            sources = listOf(source)
            symbolProcessorProviders = listOf(provider)
            inheritClassPath = true
            kspArgs = mutableMapOf("module-name" to "test")
            kspWithCompilation = true
        }.compile().apply(assertion)
}

fun String.assertCompileResolver(
    assertion: (Resolver) -> Unit,
) {
    var assertionError: Throwable? = null
    val saveAssertionError: (Resolver) -> Unit = {
        try {
            assertion(it)
        } catch (e: Throwable) {
            assertionError = e
            throw e
        }
    }
    shouldCompileWithProvider(AssertionSymbolProcessorProvider(saveAssertionError)) {
        if (assertionError != null) {
            throw assertionError!!
        }
    }
}

private class AssertionSymbolProcessorProvider(
    private val assertions: (Resolver) -> Unit,
) : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return AssertionSymbolProcessor(assertions)
    }
}

private class AssertionSymbolProcessor(
    private val assertions: (Resolver) -> Unit,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        assertions(resolver)

        return emptyList()
    }
}