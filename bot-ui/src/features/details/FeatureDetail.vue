<template>
  <UiPanel :title="feature.id">
    <DisplayDynamicForm name="featureDescription"
                        :form="form"
                        :with-save-button="true"
                        @saveForm="saveDescription"/>
  </UiPanel>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/AppInjectionKeys";
import UiPanel from "@/common/designSystem/panel/UiPanel.vue";
import DisplayDynamicForm from "@/common/dynamicForm/displayForm/DisplayDynamicForm.vue";
import {autowired} from "@/common/utils/Injection.util";
import FeatureDescription from "@/features/FeatureDescription";
import {ref} from "vue";
import {useI18n} from "vue-i18n";

const props = defineProps({
  featureId: {
    type: String,
    required: true,
  },
})

const featureService = autowired(AppInjectionKeys.FEATURE_SERVICE)

const feature = ref(await loadDescription())
const form = ref(feature.value.definition)

const notificationService = autowired(AppInjectionKeys.NOTIFICATION_SERVICE)
const {t} = useI18n()

async function loadDescription(): Promise<FeatureDescription> {
  return featureService.getFeatureDescription(props.featureId);
}

async function saveDescription() {
  await featureService.updateFeature(feature.value.id, form.value).then(() => {
    notificationService.success(t("common.success"))
  }).catch(() => {
    notificationService.error(t("common.error"))
  })
}

</script>