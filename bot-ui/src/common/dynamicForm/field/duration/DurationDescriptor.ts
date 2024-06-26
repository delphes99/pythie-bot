import DurationDescriptorEditView from "@/common/dynamicForm/field/duration/DurationDescriptorEditView.vue";
import {FieldDescriptor} from "@/common/dynamicForm/field/FieldDescriptor";
import {DescriptorJsonType} from "@/common/dynamicForm/field/FieldDescriptor.factory";
import {FieldDescriptorType} from "@/common/dynamicForm/field/FieldDescriptorType";
import {FieldJsonValue} from "@/common/dynamicForm/field/FieldJsonValue";
import {Duration, formatDuration, parseDuration} from "@/common/utils/Duration";

export class DurationDescriptor implements FieldDescriptor<string> {
    static readonly type: FieldDescriptorType = FieldDescriptorType.DURATION

    constructor(
        readonly description: string,
        readonly fieldName: string,
        readonly initialValue: Duration,
        readonly actualValue: Duration = initialValue
    ) {
    }

    withHours(hours: number): DurationDescriptor {
        return new DurationDescriptor(
            this.description,
            this.fieldName,
            this.initialValue,
            this.actualValue.withHours(hours)
        );
    }

    withMinutes(minutes: number): DurationDescriptor {
        return new DurationDescriptor(
            this.description,
            this.fieldName,
            this.initialValue,
            this.actualValue.withMinutes(minutes)
        );
    }

    withSeconds(seconds: number): DurationDescriptor {
        return new DurationDescriptor(
            this.description,
            this.fieldName,
            this.initialValue,
            this.actualValue.withSeconds(seconds)
        );
    }

    static fromJson(json: DescriptorJsonType): DurationDescriptor {
        if (json.type !== this.type) {
            throw new Error(`Cannot deserialize ${json.type} as DurationDescriptor`);
        }
        return new DurationDescriptor(
            json.description,
            json.fieldName,
            parseDuration(json.value)
        );
    }

    buildValue() {
        return new FieldJsonValue(this.fieldName, formatDuration(this.actualValue));
    }

    viewComponent() {
        return DurationDescriptorEditView
    }
}