import {FieldDescriptor, FieldValue} from "@/common/dynamicForm/field-descriptor";

export class TestFieldDescriptor implements FieldDescriptor<string> {
    constructor(readonly fieldName: string,
                readonly value: string = "value",
                readonly description: string = "description") {
    }

    buildValue(): FieldValue<string> {
        return new FieldValue(this.fieldName, this.value);
    }

    viewComponent(): any {
        return null;
    }
}