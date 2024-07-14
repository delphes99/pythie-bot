package fr.delphes.generation

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspIncremental
import com.tschuchort.compiletesting.kspLoggingLevels
import com.tschuchort.compiletesting.kspProcessorOptions
import com.tschuchort.compiletesting.kspWithCompilation
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity

fun String.shouldCompileWithProvider(
    fileName: String,
    provider: SymbolProcessorProvider,
    assertion: JvmCompilationResult.() -> Unit,
) {
    this.shouldCompileWithProvider(fileName, listOf(provider), assertion)
}

fun String.shouldCompileWithProvider(
    fileName: String,
    providers: List<SymbolProcessorProvider>,
    assertion: JvmCompilationResult.() -> Unit,
) {
    val source = SourceFile.kotlin(
        fileName, this
    )
    source.shouldCompileWithProvider(providers, assertion)
}

fun SourceFile.shouldCompileWithProvider(
    providers: List<SymbolProcessorProvider>,
    assertion: JvmCompilationResult.() -> Unit,
) {
    KotlinCompilation()
        .apply {
            sources = listOf(this@shouldCompileWithProvider)
            symbolProcessorProviders += providers
            inheritClassPath = true
            kspWithCompilation = true
            kspIncremental = true
            kspLoggingLevels += CompilerMessageSeverity.LOGGING
            kspLoggingLevels += CompilerMessageSeverity.INFO
            kspLoggingLevels += CompilerMessageSeverity.WARNING
            kspLoggingLevels += CompilerMessageSeverity.ERROR
            kspProcessorOptions.put("module-name", "test")
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
    shouldCompileWithProvider("MyEvent.kt", AssertionSymbolProcessorProvider(saveAssertionError)) {
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