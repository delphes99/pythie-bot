import {FieldValue} from "@/common/dynamicForm/field/FieldDescriptor";
import {FieldDescriptorType} from "@/common/dynamicForm/field/FieldDescriptorType";
import {MapDescriptor} from "@/common/dynamicForm/field/map/MapDescriptor";
import {describe, expect, it} from "vitest";

describe("Map descriptor", () => {
    it("construct from JSON", () => {
        const descriptor = MapDescriptor.fromJson({
            type: FieldDescriptorType.MAP,
            fieldName: "fieldName",
            description: "description",
            value: '{"key": "value", "key2": "value2"}',
        });

        expect(descriptor).toStrictEqual(
            new MapDescriptor(
                "description",
                "fieldName",
                '{"key": "value", "key2": "value2"}'
            )
        )
    })
    it("build with actual value", () => {
        const descriptor = new MapDescriptor("description", "fieldName", '{"key": "value", "key2": "value2"}');

        const value = descriptor.buildValue();

        expect(value).toStrictEqual(new FieldValue("fieldName", '{"key": "value", "key2": "value2"}'));
    })
    it("copy with new value", () => {
        const descriptor = new MapDescriptor("description", "fieldName", '{"key": "value", "key2": "value2"}');

        const newDescriptor = descriptor.withValue('{"key": "newValue", "key2": "value2"}').buildValue();

        expect(newDescriptor).toStrictEqual(new FieldValue("fieldName", '{"key": "newValue", "key2": "value2"}'));
    });
});