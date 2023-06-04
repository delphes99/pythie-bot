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
import {AppInjectionKeys} from "@/app.injection.keys";
import {FieldDescriptor} from "@/common/describableForm/field-descriptor";
import UiButton from "@/common/designSystem/button/UiButton.vue";
import {autowired} from "@/common/utils/injection.util";
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