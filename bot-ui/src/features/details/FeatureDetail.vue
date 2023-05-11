<template>
    <div v-for="descriptor in featureDescription.descriptors" :key="descriptor.fieldName">
        <component :is="descriptor.viewComponent()"
                   :descriptor="descriptor"
                   @modifyDescriptor="modifyDescriptor"
        />
    </div>
    <ui-button label="common.save" @click="saveDescription"/>
</template>

<script lang="ts" setup>
import UiButton from "@/common/components/common/button/UiButton.vue";
import {FieldDescriptor} from "@/common/describable-form/field-descriptor";
import {NotificationService} from "@/common/notification/notification.service";
import FeatureService from "@/features/feature.service";
import {InjectionKeys} from "@/injection.keys";
import {inject, ref} from "vue";
import {useI18n} from "vue-i18n";

const props = defineProps({
    featureId: {
        type: String,
        required: true,
    },
})

const featureService = inject(InjectionKeys.FEATURE_SERVICE) as FeatureService

const featureDescription = ref(await loadDescription())
const notificationService = inject(InjectionKeys.NOTIFICATION_SERVICE) as NotificationService
const {t} = useI18n()

async function loadDescription() {
    return featureService.getFeatureDescription(props.featureId);
}

function modifyDescriptor(modifiedDescriptor: FieldDescriptor<any>) {
    featureDescription.value = featureDescription.value.modifyDescriptor(modifiedDescriptor)
}

async function saveDescription() {
    const featureConfiguration = featureDescription.value.buildValue();

    await featureService.updateFeature(featureConfiguration).then(() => {
        notificationService.success(t("common.success"))
    }).catch(() => {
        notificationService.error(t("common.error"))
    })
}

</script>