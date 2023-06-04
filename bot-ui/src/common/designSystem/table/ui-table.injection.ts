import {ColumnDefinition} from "@/common/designSystem/table/ColumnDefinition";
import {InjectionKey} from "vue";

export const UiTableInjectionKeys = {
    COLUMN_REGISTRATION: Symbol() as InjectionKey<{
        registerColumn: (column: ColumnDefinition) => void,
        unregisterColumn: (columnId: string) => void,
    }>
}