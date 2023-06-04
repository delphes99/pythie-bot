import {TransformValue} from "@/common/designSystem/table/ui-tabble.column.definition";

export function getColumnValue(item: unknown, propertyName: string): string | undefined {
    if (propertyName) {
        const ext = propertyName.split(".");
        let fieldValue: any = item;
        for (const field of ext) {
            fieldValue = fieldValue?.[field];
            if (!fieldValue) {
                return undefined;
            }
        }
        return fieldValue;
    }

    return undefined;
}

export function getExtractColumnValue(propertyName?: string, transform?: TransformValue): TransformValue | undefined {
    if (propertyName) {
        return (item) => {
            const value = getColumnValue(item, propertyName!);
            if (transform) {
                return transform(value);
            } else {
                return value;
            }
        }
    }

    return undefined
}
