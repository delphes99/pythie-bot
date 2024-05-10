package fr.delphes.generation.dynamicForm

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import fr.delphes.annotation.dynamicForm.DynamicFormFamily
import fr.delphes.dynamicForm.DynamicFormDTO
import fr.delphes.dynamicForm.descriptor.FieldDescriptor
import fr.delphes.dynamicForm.descriptor.FormListFieldDescriptor
import fr.delphes.dynamicForm.descriptor.StringFieldDescriptor
import kotlinx.serialization.Contextual
import kotlin.reflect.KClass

sealed class FieldMetadata {
    abstract val name: String
    abstract val description: String
    abstract val descriptionClass: KClass<out FieldDescriptor>
    abstract val fieldType: TypeName
    abstract val defaultValue: String
}

data class FieldWithType(
    override val name: String,
    override val description: String,
    val serializer: KClass<*>?,
    override val defaultValue: String,
    override val descriptionClass: KClass<out FieldDescriptor>,
    override val fieldType: TypeName,
) : FieldMetadata()


data class FieldWithFormList(
    override val name: String,
    override val description: String,
    val formFamily: DynamicFormFamily,
) : FieldMetadata() {
    val serializer = null
    override val defaultValue = "emptyList()"
    override val descriptionClass = FormListFieldDescriptor::class
    override val fieldType: TypeName = List::class.asTypeName().parameterizedBy(
        DynamicFormDTO::class.asTypeName().parameterizedBy(
            Any::class.asTypeName().copy(
                annotations =
                listOf(AnnotationSpec.builder(Contextual::class).build())
            )
        )
    )
}

data class FieldWithMapper(
    override val name: String,
    override val description: String,
    val mapperClass: KSType,
) : FieldMetadata() {
    override val descriptionClass = StringFieldDescriptor::class
    override val fieldType = String::class.asTypeName()
    override val defaultValue = "\"\""
}