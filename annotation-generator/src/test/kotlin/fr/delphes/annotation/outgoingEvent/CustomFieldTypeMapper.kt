package fr.delphes.annotation.outgoingEvent

import fr.delphes.annotation.outgoingEvent.createBuilder.FieldDescriptorMapper
import fr.delphes.state.StateProvider

object CustomFieldTypeMapper : FieldDescriptorMapper<CustomFieldType> {
    override suspend fun map(value: String, stateProvider: StateProvider): CustomFieldType {
        return CustomFieldType(value)
    }
}