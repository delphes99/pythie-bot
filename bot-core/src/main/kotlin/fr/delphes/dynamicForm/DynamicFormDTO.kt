package fr.delphes.dynamicForm

interface DynamicFormDTO<out T> : DynamicFormDTOForSerialization {
    //    val type: DynamicFormType
    fun description(): DynamicFormDescription
    fun build(): T
}