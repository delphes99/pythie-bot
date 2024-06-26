package fr.delphes.generation.outgoingEvent.generateBuilderProcessor

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.toClassName
import fr.delphes.generation.dynamicForm.FieldMetadata
import fr.delphes.generation.dynamicForm.FieldWithFormList
import fr.delphes.generation.dynamicForm.FieldWithMapper
import fr.delphes.generation.dynamicForm.FieldWithType

object FieldDescriptionFactory {
    fun buildDescription(builder: FunSpec.Builder, property: FieldMetadata) {
        with(builder) {
            when (property) {
                is FieldWithType,
                is FieldWithMapper,
                -> {
                    addCode("%T(", property.descriptionClass)
                    addStatement("fieldName=\"${property.name}\",")
                    addStatement("description=\"${property.description}\",")
                    addStatement("value=${property.name},")
                    addCode("),\n")
                }

                is FieldWithFormList -> {
                    addCode("%T(", property.descriptionClass)
                    addStatement("fieldName=\"${property.name}\",")
                    addStatement("description=\"${property.description}\",")
                    addStatement("formFamily=\"${property.formFamily}\",")
                    addStatement("value=${property.name},")
                    addCode("),\n")
                }
            }
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

            is FieldWithFormList -> TODO()
        }
    }
}