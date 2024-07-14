<template>
  <UiCardPanel title="features.title">
    <template #header>
      <UiButton
          label="common.add"
          @on-click="openCreateModal"
      />
    </template>
    <FeatureCard
        v-for="feature in features"
        :key="feature.id"
        :feature="feature"
    />
  </UiCardPanel>
  <UiModal
      v-model:is-open="isCreateModalOpened"
      title="Create feature">
    <UiTextfield v-model="featureId" label="feature.id"/>
    <UiSelect
        v-model="selectedType"
        label="feature.type"
        :options="allTypesAsSelectOptions"
    />
    <UiButton label="common.add" @on-click="createFeature"/>
  </UiModal>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/AppInjectionKeys";
import UiButton from "@/common/designSystem/button/UiButton.vue";
import UiCardPanel from "@/common/designSystem/card/UiCardPanel.vue"
import UiSelect from "@/common/designSystem/form/select/UiSelect.vue";
import UiTextfield from "@/common/designSystem/form/textfield/UiTextfield.vue";
import UiModal from "@/common/designSystem/modal/UiModal.vue";
import {useModal} from "@/common/designSystem/modal/useModal";
import {autowired} from "@/common/utils/Injection.util";
import FeatureCard from "@/features/featureCard/FeatureCard.vue"
import {FeatureType} from "@/features/FeatureService";
import {FeatureSummary} from "@/features/FeatureSummary";
import {useGetFeatureTypes} from "@/features/useGetFeatureTypes";
import router from "@/router";
import {ref} from "vue"

const featureService = autowired(AppInjectionKeys.FEATURE_SERVICE)

const featureId = ref("")

const features = ref<FeatureSummary[]>([])

async function getFeatures() {
  features.value = await featureService.getAllFeatures()
}

const {allTypesAsSelectOptions, allTypes} = useGetFeatureTypes()
const selectedType = ref<FeatureType>(allTypes.value[0])

const {isOpen: isCreateModalOpened, open: openCreateModal} = useModal()

async function createFeature() {
  await featureService.createFeature(featureId.value, selectedType.value)
  await router.push({path: `/feature/${featureId.value}`})
}

getFeatures()
</script>