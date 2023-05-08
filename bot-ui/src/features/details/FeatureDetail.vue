<template>
    <div v-for="descriptor in featureDescription.descriptors" :key="descriptor.fieldName">
        <component :is="descriptor.viewComponent()"
                   :descriptor="descriptor"
        />
    </div>
    <ui-button label="common.save" @click="saveDescription"/>
</template>

<script lang="ts" setup>
import {NotificationService} from "@/common/notification/notification.service";
import UiButton from "@/ds/button/UiButton.vue";
import FeatureService from "@/features/feature.service";
import {InjectionKeys} from "@/injection.keys";
import {inject} from "vue";
import {useI18n} from "vue-i18n";

const props = defineProps({
    featureId: {
        type: String,
        required: true,
    },
})

const featureService = inject(InjectionKeys.FEATURE_SERVICE) as FeatureService

const featureDescription = await loadDescription()
const notificationService = inject(InjectionKeys.NOTIFICATION_SERVICE) as NotificationService
const {t} = useI18n()

async function loadDescription() {
    return featureService.getFeatureDescription(props.featureId);
}

async function saveDescription() {
    const featureConfiguration = featureDescription.buildValue();

    await featureService.updateFeature(featureConfiguration).then(() => {
        notificationService.success(t("common.success"))
    }).catch(() => {
        notificationService.error(t("common.error"))
    })
}

</script>