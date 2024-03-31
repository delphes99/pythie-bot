package fr.delphes.annotation.outgoingEvent.createBuilder

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.toClassName
import fr.delphes.dynamicForm.FieldMetadata
import fr.delphes.dynamicForm.FieldWithMapper
import fr.delphes.dynamicForm.FieldWithType

object FieldDescriptionFactory {
    fun buildDescription(builder: FunSpec.Builder, property: FieldMetadata) {
        with(builder) {
            addCode("%T(", property.descriptionClass)
            addStatement("fieldName=\"${property.name}\",")
            addStatement("description=\"${property.description}\",")
            addStatement("value=${property.name},")
            addCode("),\n")
        }
    }

    fun buildEncodeValue(builder: FunSpec.Builder, property: FieldMetadata) {
        when (property) {
            is FieldWithMapper -> {
                builder.addCode(
                    "%T.map(this.${property.name})", property.mapperClass.toClassName()
                )
            }

            is FieldWithType -> {
                builder.addCode("this.${property.name}")
            }
        }
    }
}