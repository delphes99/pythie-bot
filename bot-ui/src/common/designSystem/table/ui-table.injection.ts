import {UiTabbleColumnDefinition} from "@/common/designSystem/table/ui-tabble.column.definition";
import {InjectionKey} from "vue";

export const UiTableInjectionKeys = {
    COLUMN_REGISTRATION: Symbol() as InjectionKey<{
        registerColumn: (column: UiTabbleColumnDefinition) => void,
        unregisterColumn: (columnId: string) => void,
    }>
}