import FieldDurationEditView from "@/common/describableForm/duration/field-duration-edit-view.vue";
import {FieldDescriptor, FieldValue} from "@/common/describableForm/field-descriptor";
import {FieldDescriptorType} from "@/common/describableForm/field-descriptor-type";
import {DescriptorJsonType} from "@/common/describableForm/field-descriptor.factory";
import {Duration, formatDuration, parseDuration} from "@/common/utils/duration.utils";

export class DurationDescriptor implements FieldDescriptor<string> {
    readonly type: FieldDescriptorType = FieldDescriptorType.DURATION

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
        if (json.type !== FieldDescriptorType.DURATION) {
            throw new Error(`Cannot deserialize ${json.type} as DurationDescriptor`);
        }
        return new DurationDescriptor(
            json.description,
            json.fieldName,
            parseDuration(json.value)
        );
    }

    buildValue() {
        return new FieldValue(this.fieldName, formatDuration(this.actualValue));
    }

    viewComponent() {
        return FieldDurationEditView
    }
}