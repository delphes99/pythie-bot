import {FieldDescriptor, FieldValue} from "@/common/describable-form/field-descriptor";

export class TestFieldDescriptor implements FieldDescriptor<string> {
    constructor(readonly fieldName: string,
                readonly description: string,
                readonly value: string) {
    }

    buildValue(): FieldValue<string> {
        return new FieldValue(this.fieldName, this.value);
    }

    viewComponent(): any {
        return null;
    }
}