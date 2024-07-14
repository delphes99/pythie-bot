package fr.delphes.dynamicForm

interface DynamicFormDTO<out T> {
    //    val type: DynamicFormType
    fun description(): DynamicFormDescription
    fun build(): T
}