<template>
    <div v-for="descriptor in fieldDescriptors" :key="descriptor.descriptor.fieldName">
        <ui-textfield
                :id="descriptor.descriptor.fieldName"
                :label="descriptor.descriptor.description"
                v-model="descriptor.currentValue"
        />
    </div>
    <ui-button label="common.save" @click="saveDescription"/>
</template>

<script setup lang="ts">
import {inject} from "vue";
import {InjectKey} from "@/main";
import FeatureService from "@/features/feature.service";
import {FeatureDescriptor} from "@/features/feature-description";
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue";
import FeatureDescriptionService, {SetValue} from "@/features/feature-description.service";
import UiButton from "@/ds/button/UiButton.vue";

class FieldDescriptor {
    descriptor: FeatureDescriptor
    currentValue: string

    constructor(descriptor: FeatureDescriptor, currentValue: string) {
        this.descriptor = descriptor;
        this.currentValue = currentValue;
    }
}

const props = defineProps({
    featureId: {
        type: String,
        required: true,
    },
})

const featureService = inject(InjectKey.FEATURE_SERVICE) as FeatureService
const featureDescriptionService = inject(InjectKey.FEATURE_DESCRIPTION_SERVICE) as FeatureDescriptionService

const featureDescription = await loadDescription()

async function loadDescription() {
    return featureService.getFeatureDescription(props.featureId);
}

const fieldDescriptors = featureDescription.descriptors.map(descriptor => {
    return new FieldDescriptor(descriptor, descriptor.value)
})

async function saveDescription() {
    const modifications = fieldDescriptors.map(descriptor => new SetValue(
            descriptor.descriptor.fieldName,
            descriptor.currentValue,
        ));
    const featureConfiguration = featureDescriptionService.buildConfiguration(featureDescription, ...modifications)

    await featureService.updateFeature(featureConfiguration)
}

</script>