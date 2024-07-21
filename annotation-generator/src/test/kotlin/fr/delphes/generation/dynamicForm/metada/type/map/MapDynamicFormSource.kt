package fr.delphes.generation.dynamicForm.metada.type.map

import fr.delphes.annotation.dynamicForm.DynamicForm
import fr.delphes.annotation.dynamicForm.FieldDescription

@DynamicForm("serializeName")
class MapDynamicFormSource(
    @FieldDescription("description")
    val myField: Map<String, String>,
)