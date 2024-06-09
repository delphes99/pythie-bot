import {FieldDescriptor} from "@/common/dynamicForm/field/FieldDescriptor";
import {DescriptorJsonType} from "@/common/dynamicForm/field/FieldDescriptor.factory";
import {FieldDescriptorType} from "@/common/dynamicForm/field/FieldDescriptorType";
import {FieldJsonValue} from "@/common/dynamicForm/field/FieldJsonValue";
import MapDescriptorEditView from "./MapDescriptorEditView.vue";

export class MapDescriptor implements FieldDescriptor<string> {
    static readonly type: FieldDescriptorType = FieldDescriptorType.MAP;

    constructor(
        readonly description: string,
        readonly fieldName: string,
        readonly initialValue: string,
        readonly actualValue: string = initialValue
    ) {
    }

    withValue(newValue: string): MapDescriptor {
        return new MapDescriptor(
            this.description,
            this.fieldName,
            this.initialValue,
            newValue
        );
    }

    static fromJson(json: DescriptorJsonType): MapDescriptor {
        if (json.type !== this.type) {
            throw new Error(`Cannot deserialize ${json.type} as MapDescriptor`);
        }

        return new MapDescriptor(
            json.description,
            json.fieldName,
            JSON.stringify(json.value)
        );
    }

    buildValue() {
        return new FieldJsonValue(this.fieldName, JSON.parse(this.actualValue));
    }

    viewComponent() {
        return MapDescriptorEditView
    }
}