package fr.delphes.generation.dynamicForm.metada.type.string

import fr.delphes.annotation.dynamicForm.DynamicForm
import fr.delphes.annotation.dynamicForm.FieldDescription

@DynamicForm("serializeName")
class StringDynamicFormSource(
    @FieldDescription("description")
    val myField: String,
)