import {UiTableColumnDefinition} from "@/common/designSystem/table/UiTableColumnDefinition";
import {InjectionKey} from "vue";

export const UiTableInjectionKeys = {
    COLUMN_REGISTRATION: Symbol() as InjectionKey<{
        registerColumn: (column: UiTableColumnDefinition) => void,
        unregisterColumn: (columnId: string) => void,
    }>
}