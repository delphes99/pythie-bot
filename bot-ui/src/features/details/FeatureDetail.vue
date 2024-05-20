<template>
  <UiPanel :title="featureDescription.id">
    <div v-for="descriptor in featureDescription.descriptors" :key="descriptor.fieldName">
      <component :is="descriptor.viewComponent()"
                 :descriptor="descriptor"
                 @modifyDescriptor="modifyDescriptor"
      />
    </div>
    <UiButton label="common.save" @click="saveDescription"/>
  </UiPanel>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/AppInjectionKeys";
import UiButton from "@/common/designSystem/button/UiButton.vue";
import UiPanel from "@/common/designSystem/panel/UiPanel.vue";
import {FieldDescriptor} from "@/common/dynamicForm/field/FieldDescriptor";
import {autowired} from "@/common/utils/Injection.util";
import {ref} from "vue";
import {useI18n} from "vue-i18n";

const props = defineProps({
  featureId: {
    type: String,
    required: true,
  },
})

const featureService = autowired(AppInjectionKeys.FEATURE_SERVICE)

const featureDescription = ref(await loadDescription())
const notificationService = autowired(AppInjectionKeys.NOTIFICATION_SERVICE)
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