package fr.delphes.generation.dynamicForm.metada.type.list

import fr.delphes.annotation.dynamicForm.DynamicForm
import fr.delphes.annotation.dynamicForm.FieldDescription

@DynamicForm("serializeName")
class ListDynamicFormSource(
    @FieldDescription("description")
    val myField: List<ListInnerFormSource>,
)