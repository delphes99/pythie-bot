package fr.delphes.generation.dynamicForm.metada.type.duration

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.asTypeName
import fr.delphes.dynamicForm.descriptor.DurationFieldDescriptor
import fr.delphes.generation.dynamicForm.metada.FieldMetadata
import fr.delphes.generation.dynamicForm.metada.FieldWithType
import fr.delphes.generation.dynamicForm.metada.TypeMetadataExtractor
import fr.delphes.generation.dynamicForm.metada.getDescription
import fr.delphes.generation.dynamicForm.metada.getSimpleName
import fr.delphes.utils.serialization.DurationSerializer
import java.time.Duration

object DurationMetadaExtractor : TypeMetadataExtractor {
    override val typeName = "java.time.Duration"

    override fun extract(declaration: KSPropertyDeclaration): FieldMetadata {
        return FieldWithType(
            declaration.getSimpleName(),
            declaration.getDescription(),
            DurationSerializer::class,
            "Duration.ZERO",
            DurationFieldDescriptor::class,
            Duration::class.asTypeName()
        )
    }
}