import {FieldDescriptor} from "@/common/dynamicForm/field/FieldDescriptor";
import {FieldJsonValue} from "@/common/dynamicForm/field/FieldJsonValue";

export class TestFieldDescriptor implements FieldDescriptor<string> {
    constructor(readonly fieldName: string,
                readonly value: string = "value",
                readonly description: string = "description") {
    }

    buildValue() {
        return new FieldJsonValue(this.fieldName, this.value);
    }

    viewComponent(): any {
        return null;
    }
}