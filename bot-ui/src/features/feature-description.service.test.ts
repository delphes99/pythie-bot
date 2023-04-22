import {describe, expect, it} from "vitest";
import FeatureDescription, {FeatureConfiguration} from "@/features/feature-description";
import FeatureDescriptionService, {SetValue} from "@/features/feature-description.service";
import {FieldDescriptor} from "@/common/describable-form/field-descriptor";
import {FieldDescriptorType} from "@/common/describable-form/field-descriptor-type";

const descriptionService = new FeatureDescriptionService()

describe("FeatureDescriptionService", () => {
    const descriptor = new FeatureDescription(
        "type",
        "some-id",
        [
            new FieldDescriptor(
                "fieldName",
                "description",
                FieldDescriptorType.STRING,
                "value for field 1"
            ),
            new FieldDescriptor(
                "fieldName2",
                "description",
                FieldDescriptorType.STRING,
                "value for field 2"
            ),
            new FieldDescriptor(
                "otherFieldName",
                "description",
                FieldDescriptorType.STRING,
                "value for other field"
            )
        ]
    )

    it("configuration without modification", () => {
        const expectedPayload = {
            id: "some-id",
            type: "type",
            fieldName: "value for field 1",
            fieldName2: "value for field 2",
            otherFieldName: "value for other field"
        }

        const payload = descriptionService.buildConfiguration(descriptor);

        expect(payload).toEqual(expectedPayload)
    })

    it("configuration with modification", () => {
        const expectedPayload: FeatureConfiguration = {
            id: "some-id",
            type: "type",
            fieldName: "new value",
            fieldName2: "value for field 2",
            otherFieldName: "new value for other field"
        }

        const payload = descriptionService.buildConfiguration(
            descriptor,
            new SetValue("fieldName", "new value"),
            new SetValue("otherFieldName", "new value for other field")
        )

        expect(payload).toEqual(expectedPayload)
    })
})
