<template>
    <div v-for="descriptor in featureDescription.descriptors" :key="descriptor.fieldName">
        <component :is="descriptor.viewComponent()"
                   :descriptor="descriptor"
        />
    </div>
    <ui-button label="common.save" @click="saveDescription"/>
</template>

<script setup lang="ts">
import {inject} from "vue";
import {InjectKey} from "@/main";
import FeatureService from "@/features/feature.service";
import UiButton from "@/ds/button/UiButton.vue";
const props = defineProps({
    featureId: {
        type: String,
        required: true,
    },
})

const featureService = inject(InjectKey.FEATURE_SERVICE) as FeatureService

const featureDescription = await loadDescription()

async function loadDescription() {
    return featureService.getFeatureDescription(props.featureId);
}

async function saveDescription() {
    console.log(featureDescription)
    /*const modifications = fieldDescriptors.map(descriptor => new SetValue(
        descriptor.descriptor.fieldName,
        descriptor.currentValue,
    ));
    const featureConfiguration = featureDescriptionService.buildConfiguration(featureDescription, ...modifications)

    await featureService.updateFeature(featureConfiguration)*/
}

</script>