import DynamicFormService from "@/common/dynamicForm/DynamicFormService";
import {FieldDuration} from "@/common/dynamicForm/field/duration/FieldDuration";
import {Field} from "@/common/dynamicForm/field/Field";
import {FieldType} from "@/common/dynamicForm/field/FieldType";
import {FieldFormList} from "@/common/dynamicForm/field/formList/FieldFormList";
import {FieldString} from "@/common/dynamicForm/field/string/FieldString";

export type FieldJsonType = {
    type: FieldType;
    fieldName: string;
    value: any;
    [key: string]: any;
};

export function fieldFromJson(json: FieldJsonType, dynamicFormService: DynamicFormService): Field<any> {
    switch (json.type) {
        case FieldType.STRING:
            return FieldString.fromJson(json)
        case FieldType.DURATION:
            return FieldDuration.fromJson(json)
        case FieldType.FORM_LIST:
            return FieldFormList.fromJson(json, dynamicFormService)
    }
}