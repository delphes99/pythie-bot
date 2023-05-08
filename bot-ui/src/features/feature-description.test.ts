import FeatureDescription from "@/features/feature-description";
import {TestFieldDescriptor} from "@/common/describable-form/test-field-descriptor.util.test";
import {describe, expect, it} from "vitest";

describe("Feature description", () => {
    it("build value should return id and type in json format", () => {
        const featureDescription = new FeatureDescription(
            "type",
            "id",
            []
        );
        expect(featureDescription.buildValue()).toStrictEqual({
            id: "id",
            type: "type",
        });
    })
    it("build value should return field values in json format", () => {
        const featureDescription = new FeatureDescription(
            "type",
            "id",
            [
                new TestFieldDescriptor("fieldName", "description", "first value"),
                new TestFieldDescriptor("anotherField", "bla bla", "second value"),
            ]
        );
        expect(featureDescription.buildValue()).toStrictEqual({
            id: "id",
            type: "type",
            fieldName: "first value",
            anotherField: "second value",
        });
    })
});

