<template>
  <ui-card-panel title="features.title">
    <template #header>
      <ui-button
          label="common.add"
          @on-click="openCreateModal"
      />
    </template>
    <feature-card
        v-for="feature in features"
        :key="feature"
        :feature="feature"
    />
  </ui-card-panel>
  <ui-modal
      v-model:is-open="isCreateModalOpened"
      title="Create feature">
    <ui-textfield v-model="featureId" label="feature.id"/>
    <ui-select
        v-model="selectedType"
        label="feature.type"
        :options="allTypesAsSelectOptions"
    />
    <ui-button label="common.add" @on-click="createFeature"/>
  </ui-modal>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/app.injection.keys";
import UiButton from "@/common/designSystem/button/ui-button.vue";
import UiCardPanel from "@/common/designSystem/card/ui-card-panel.vue"
import UiSelect from "@/common/designSystem/form/select/ui-select.vue";
import UiTextfield from "@/common/designSystem/form/textfield/ui-textfield.vue";
import UiModal from "@/common/designSystem/modal/ui-modal.vue";
import {useModal} from "@/common/designSystem/modal/useModal";
import {autowired} from "@/common/utils/injection.util";
import {Feature} from "@/features/feature";
import FeatureService, {FeatureType} from "@/features/feature.service";
import FeatureCard from "@/features/featureCard/feature-card.vue"
import {useGetFeatureTypes} from "@/features/useGetFeatureTypes";
import router from "@/router";
import {ref} from "vue"
import {useI18n} from "vue-i18n"

const {t} = useI18n()

const backendUrl = autowired(AppInjectionKeys.BACKEND_URL)

const featureId = ref("")

const features = ref<Feature[]>([])

async function getFeatures() {
  const response = await fetch(`${backendUrl}/features`)

  features.value = await response.json()
}

const {allTypesAsSelectOptions, allTypes} = useGetFeatureTypes()
const selectedType = ref<FeatureType>(allTypes.value[0])

const {isOpen: isCreateModalOpened, open: openCreateModal} = useModal()

async function createFeature() {
  await new FeatureService(backendUrl).createFeature(featureId.value, selectedType.value)
  await router.push({path: `/feature/${featureId.value}`})
}

getFeatures()
</script>