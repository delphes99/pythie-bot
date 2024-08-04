package fr.delphes.generation.dynamicForm.metadata.type.duration

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.asTypeName
import fr.delphes.dynamicForm.descriptor.DurationFieldDescriptor
import fr.delphes.generation.dynamicForm.metadata.FieldMetadata
import fr.delphes.generation.dynamicForm.metadata.FieldWithType
import fr.delphes.generation.dynamicForm.metadata.TypeMetadataExtractor
import fr.delphes.generation.dynamicForm.metadata.getDescription
import fr.delphes.generation.dynamicForm.metadata.getSimpleName
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