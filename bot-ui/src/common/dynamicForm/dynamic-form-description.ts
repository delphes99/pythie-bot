import {FieldDescriptor} from "@/common/dynamicForm/field/field-descriptor";

export interface DynamicFormDescription {
    readonly type: string
    readonly descriptors: FieldDescriptor<any>[]
}