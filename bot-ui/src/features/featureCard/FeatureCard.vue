<template>
  <ui-card :title="feature.identifier">
    <div>Type : {{ $t("configuration.features." + feature.type) }}</div>
    <div
      v-for="item in viewForm"
      :key="item.id"
    >
      <component
        :is="item.viewComponent()"
        :item="item"
      />
    </div>
    <template #actions>
      <ui-button
        label="common.edit"
        @on-click="openSettings"
      />
    </template>
  </ui-card>
  <ui-modal
    v-model:is-open="isSettingOpened"
    :title="feature.identifier"
  >
    <fieldset class="flex flex-col border border-black p-1">
      <legend>Edit</legend>
      <div
        v-for="(item, index) in editForm"
        :key="item.field"
      >
        <component
          :is="item.editComponent()"
          v-model="editForm[index]"
        />
      </div>
    </fieldset>
    <ui-button
      label="common.save"
      @on-click="saveChanges"
    />
  </ui-modal>
</template>

<script setup lang="ts">
import UiButton from "@/ds/button/UiButton.vue"
import UiCard from "@/ds/card/UiCard.vue"
import UiModal from "@/ds/modal/UiModal.vue"
import { useModal } from "@/ds/modal/useModal"
import Feature from "@/features/configurations/Feature"
import { FormItem } from "@/features/featureCard/description/FormItem"
import { mapToFormItems } from "@/features/featureCard/description/formItemMapper"
import axios from "axios"
import { ElNotification } from "element-plus"
import { inject, PropType, ref } from "vue"
import { useI18n } from "vue-i18n"

const props = defineProps({
  feature: {
    type: Object as PropType<Feature>,
    required: true,
  },
})

const backendUrl = inject("backendUrl") as string
const { t } = useI18n()
const { isOpen: isSettingOpened, open: openSettings } = useModal()

const viewForm: FormItem[] = mapToFormItems(props.feature)
const editForm = ref<FormItem[]>(mapToFormItems(props.feature))

async function saveChanges() {
  if (editForm.value.some((item) => item.isEmpty())) {
    ElNotification({
      title: t("common.emptyField"),
      type: "error",
    })
    return
  }

  const { type } = props.feature
  const newFeature: any = { type }

  editForm.value.forEach((item) => {
    item.appendToResult(newFeature)
  })

  await axios.post(`${backendUrl}/feature/edit`, JSON.stringify(newFeature), {
    headers: { "Content-Type": "application/json" },
  })
}
</script>