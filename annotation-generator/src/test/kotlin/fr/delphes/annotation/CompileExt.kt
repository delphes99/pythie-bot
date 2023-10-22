package fr.delphes.annotation

import com.google.devtools.ksp.processing.SymbolProcessorProvider
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