<template>
  <ui-panel
    title="features.title"
    :menu="[UiPanelMenuItem.of('common.add', openNewFeature)]"
  >
    <ui-card-panel>
      <feature-card
        v-for="feature in features"
        :key="feature"
        :feature="feature"
      />
    </ui-card-panel>
  </ui-panel>
  <ui-modal
    v-model:is-open="isNewFeatureOpened"
    title="features.addFeature"
  >
    <ui-textfield
      v-model="featureToAddName"
      label="feature.name"
    />
    <ui-select
      v-model="featureToAdd"
      :options="availableFeatureTypes"
      label="feature.type"
    />
    <ui-button
      :type="UiButtonType.Primary"
      label="common.add"
      @on-click="newFeature()"
    />
  </ui-modal>
</template>

<script setup lang="ts">
import UiCardPanel from "@/common/components/common/card/UiCardPanel.vue"
import UiButton from "@/ds/button/UiButton.vue"
import { UiButtonType } from "@/ds/button/UiButtonType"
import UiSelect from "@/ds/form/select/UiSelect.vue"
import { UiSelectOption } from "@/ds/form/select/UiSelectOption"
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import UiModal from "@/ds/modal/UiModal.vue"
import { useModal } from "@/ds/modal/useModal"
import UiPanel from "@/ds/panel/UiPanel.vue"
import { UiPanelMenuItem } from "@/ds/panel/UiPanelMenuItem"
import { fromJson } from "@/features/configurations/Feature"
import FeatureType from "@/features/configurations/FeatureType"
import FeatureCard from "@/features/featureCard/FeatureCard.vue"
import { ElNotification } from "element-plus"
import { inject, ref } from "vue"
import { useI18n } from "vue-i18n"

const { t } = useI18n()

const backendUrl = inject("backendUrl") as string
const features = ref([])

async function getFeatures() {
  const response = await fetch(`${backendUrl}/features`)

  features.value = await response.json().then((data) => data.map(fromJson))
}

getFeatures()

const {
  isOpen: isNewFeatureOpened,
  open: openNewFeature,
  close: closeNewFeature,
} = useModal()

const availableFeatureTypes = UiSelectOption.for(
  Object.values(FeatureType),
  (value: FeatureType) => t("configuration.features." + value),
)
const featureToAdd = ref<FeatureType | null>(null)
const featureToAddName = ref("")

const newFeature = async () => {
  if (featureToAdd.value && featureToAddName) {
    const content = JSON.stringify({
      name: featureToAddName.value,
      type: featureToAdd.value,
    })

    return await fetch(`${backendUrl}/feature/create`, {
      method: "post",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: content,
    })
      //TODO factorize response handle
      .then((response) => {
        if (response.ok) {
          ElNotification({
            title: t("common.success"),
            type: "success",
          })
          getFeatures()
          closeNewFeature()
        } else {
          ElNotification({
            title: t("common.error"),
            type: "error",
          })
        }
      })
      .catch(() => {
        ElNotification({
          title: t("common.error"),
          type: "error",
        })
      })
  }
}
</script>