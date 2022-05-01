<template>
  <card :title="feature.identifier">
    <div>Type : {{ feature.type }}</div>
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
      <button
        class="primary-button"
        @click="openSettings()"
      >
        {{ $t("common.edit") }}
      </button>
    </template>
  </card>
  <modal
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
    <button
      class="primary-button"
      @click="saveChanges"
    >
      {{ $t("common.save") }}
    </button>
  </modal>
</template>

<script setup lang="ts">
import Card from "@/common/components/common/Card.vue"
import Modal from "@/common/components/common/CommonModal.vue"
import { FormItem } from "@/features/components/description/FormItem.js"
import { mapToFormItems } from "@/features/components/description/formItemMapper"
import Feature from "@/features/configurations/Feature"
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
const isSettingOpened = ref(false)
const openSettings = () => (isSettingOpened.value = true)
const viewForm = mapToFormItems(props.feature)
const editForm = ref<FormItem[]>(mapToFormItems(props.feature))

async function saveChanges() {
  if (editForm.value.some((item) => item.isEmpty())) {
    ElNotification({
      title: t("common.emptyField"),
      type: "error",
    })
    return
  }

  const { identifier, type } = props.feature
  const newFeature: any = { identifier, type }

  editForm.value.forEach((item) => {
    item.appendToResult(newFeature)
  })

  await axios.post(`${backendUrl}/feature/edit`, JSON.stringify(newFeature), {
    headers: { "Content-Type": "application/json" },
  })
}
</script>